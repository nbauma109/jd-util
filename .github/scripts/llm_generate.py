#!/usr/bin/env python3
import os, json, requests, pathlib, re, sys

OLLAMA_URL = os.environ.get("OLLAMA_HOST", "http://localhost:11434")
MODEL = os.environ.get("LLM_MODEL", "qwen2.5-coder:3b")

prompt = pathlib.Path(".llm_prompt.txt").read_text(encoding="utf-8") if pathlib.Path(".llm_prompt.txt").exists() else ""
ctx = pathlib.Path(".llm_context.txt").read_text(encoding="utf-8", errors="ignore") if pathlib.Path(".llm_context.txt").exists() else ""
raw_path = pathlib.Path(".llm_raw.txt")

SYSTEM = (
    "You must output ONLY a patch.\n"
    "- Prefer a git-style unified diff starting with lines like '--- a/path' then '+++ b/path'.\n"
    "- It's also acceptable to output a 'diff --git a/... b/...' patch.\n"
    "- No prose. No markdown fences. No headings. No explanations.\n"
    "If absolutely no changes are warranted, output exactly: NO_CHANGES"
)

USER = (
    f"{prompt}\n\n"
    f"PROJECT SNAPSHOT (truncated files):\n{ctx}\n\n"
    "Now output ONLY the patch (unified diff or 'diff --git'), or NO_CHANGES:"
)

payload = {
    "model": MODEL,
    "system": SYSTEM,
    "prompt": USER,
    "options": {
        "temperature": 0.0,       # be deterministic & conservative
        "num_ctx": 8192
    },
}

def call_ollama():
    with requests.post(f"{OLLAMA_URL}/api/generate", json=payload, stream=True, timeout=1200) as r:
        r.raise_for_status()
        chunks=[]
        for line in r.iter_lines():
            if not line: continue
            obj=json.loads(line.decode("utf-8"))
            if "response" in obj: chunks.append(obj["response"])
            if obj.get("done"): break
    return "".join(chunks).strip()

def strip_fences(s: str) -> str:
    s = re.sub(r"^```(?:diff|patch)?\s*", "", s, flags=re.IGNORECASE|re.MULTILINE)
    s = re.sub(r"\s*```$", "", s, flags=re.MULTILINE)
    return s

def extract_patch(s: str) -> str|None:
    if re.search(r"^\s*NO_CHANGES\s*$", s, re.IGNORECASE|re.MULTILINE):
        return "NO_CHANGES"
    m = re.search(r"^---\s+a/.*$", s, re.MULTILINE)
    if m: return s[m.start():].strip()
    m = re.search(r"^diff --git a/.* b/.*$", s, re.MULTILINE)
    if m: return s[m.start():].strip()
    m = re.search(r"^---\s+.+$", s, re.MULTILINE)
    if m and re.search(r"^\+\+\+\s+.+$", s[m.start():], re.MULTILINE):
        return s[m.start():].strip()
    return None

def main():
    raw = call_ollama()
    raw_path.write_text(raw, encoding="utf-8")

    cleaned = strip_fences(raw)
    patch = extract_patch(cleaned)

    if patch is None:
        # keep the raw for debugging; validator will print a snippet
        print(f"[llm_generate] No patch anchors found. raw_len={len(raw)}")
        return

    if re.fullmatch(r"\s*NO_CHANGES\s*", patch, re.IGNORECASE):
        raw_path.write_text("NO_CHANGES", encoding="utf-8")
        print("[llm_generate] NO_CHANGES")
        return

    raw_path.write_text(patch, encoding="utf-8")
    print(f"[llm_generate] Patch extracted: {len(patch)} bytes")

if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        sys.stderr.write(f"[llm_generate] ERROR: {e}\n")
        try: raw_path.write_text(f"ERROR: {e}", encoding="utf-8")
        except Exception: pass
        sys.exit(0)
