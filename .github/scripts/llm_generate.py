#!/usr/bin/env python3
import os, json, requests, pathlib, re, sys

OLLAMA_URL = os.environ.get("OLLAMA_HOST", "http://localhost:11434")
MODEL = os.environ.get("LLM_MODEL", "llama3.2:3b")

prompt_path = pathlib.Path(".llm_prompt.txt")
ctx_path = pathlib.Path(".llm_context.txt")
raw_path = pathlib.Path(".llm_raw.txt")

prompt = prompt_path.read_text(encoding="utf-8") if prompt_path.exists() else ""
ctx = ctx_path.read_text(encoding="utf-8", errors="ignore") if ctx_path.exists() else ""

# Stricter guardrails: absolutely no prose or fences
SYSTEM = (
    "You must output ONLY a valid git unified diff that applies with `patch -p0`.\n"
    "No explanations. No Markdown code fences. No headings. No extra text.\n"
    "If you have no changes, output exactly: NO_CHANGES"
)

USER = (
    f"{prompt}\n\n"
    f"PROJECT SNAPSHOT (truncated files):\n{ctx}\n\n"
    "Now output ONLY the unified diff (or NO_CHANGES):"
)

payload = {
    "model": MODEL,
    "system": SYSTEM,
    "prompt": USER,
    "options": {"temperature": 0.1},
}

def call_ollama():
    with requests.post(f"{OLLAMA_URL}/api/generate", json=payload, stream=True, timeout=900) as r:
        r.raise_for_status()
        chunks = []
        for line in r.iter_lines():
            if not line:
                continue
            obj = json.loads(line.decode("utf-8"))
            if "response" in obj:
                chunks.append(obj["response"])
            if obj.get("done"):
                break
    return "".join(chunks).strip()

def strip_fences(text: str) -> str:
    # remove common markdown fences (```diff, ```patch, ``` etc.)
    text = re.sub(r"^```(?:diff|patch)?\s*", "", text, flags=re.IGNORECASE | re.MULTILINE)
    text = re.sub(r"\s*```$", "", text, flags=re.MULTILINE)
    return text

def extract_unified_diff(text: str) -> str | None:
    """
    Try hard to find a unified diff.
    Preferred anchors:
      1) lines beginning with '--- a/...'
      2) generic '--- ' followed by '+++ '
      3) 'diff --git a/... b/...' blocks -> convert to unified snippet if model emitted that format
    We return everything from the first '--- ' (or 'diff --git') to the end.
    """
    # quick NO_CHANGES detection
    if re.search(r"^\s*NO_CHANGES\s*$", text, flags=re.IGNORECASE | re.MULTILINE):
        return "NO_CHANGES"

    # 1) best: --- a/...
    m = re.search(r"^---\s+a/.*$", text, flags=re.MULTILINE)
    if m:
        return text[m.start():].strip()

    # 2) fallback: first '--- ' anywhere that is part of a hunk header
    m = re.search(r"^---\s+.+$", text, flags=re.MULTILINE)
    if m:
        # ensure there's a matching '+++' after
        rest = text[m.start():]
        if re.search(r"^\+\+\+\s+.+$", rest, flags=re.MULTILINE):
            return rest.strip()

    # 3) diff --git blocks (sometimes models start with that)
    m = re.search(r"^diff --git a/.* b/.*$", text, flags=re.MULTILINE)
    if m:
        return text[m.start():].strip()

    return None

def main():
    raw = call_ollama()
    # keep a pristine copy for debugging
    raw_path.write_text(raw, encoding="utf-8")

    cleaned = strip_fences(raw)
    diff = extract_unified_diff(cleaned)

    # To keep the rest of the pipeline unchanged, write the final choice back to .llm_raw.txt
    if diff is None:
        # leave the original in .llm_raw.txt (already written), but exit 0
        print(f"[llm_generate] No diff anchors found. raw_len={len(raw)}")
        return

    # Normalize NO_CHANGES token exactly
    if re.fullmatch(r"\s*NO_CHANGES\s*", diff, flags=re.IGNORECASE):
        raw_path.write_text("NO_CHANGES", encoding="utf-8")
        print("[llm_generate] Model returned NO_CHANGES")
        return

    raw_path.write_text(diff, encoding="utf-8")
    print(f"[llm_generate] Extracted unified diff. bytes={len(diff)}")

if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        # never crash the job without a clue
        sys.stderr.write(f"[llm_generate] ERROR: {e}\n")
        # still write something for the validator to inspect
        try:
            raw_path.write_text(f"ERROR: {e}", encoding="utf-8")
        except Exception:
            pass
        sys.exit(0)
