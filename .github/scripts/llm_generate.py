#!/usr/bin/env python3
import os, json, requests, pathlib

OLLAMA_URL = os.environ.get("OLLAMA_HOST", "http://localhost:11434")
MODEL = os.environ.get("LLM_MODEL", "llama3.2:3b")

prompt = pathlib.Path(".llm_prompt.txt").read_text(encoding="utf-8")
ctx = pathlib.Path(".llm_context.txt").read_text(encoding="utf-8", errors="ignore")

system = (
    "Return ONLY a single valid unified diff that applies with `patch -p0`.\n"
    "Keep edits minimal and safe. If no changes are warranted, output NO_CHANGES."
)

payload = {
    "model": MODEL,
    "system": system,
    "prompt": f"{prompt}\n\nPROJECT SNAPSHOT (truncated files):\n{ctx}\n\nNow output the unified diff:",
    "options": {"temperature": 0.1},
}

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

txt = "".join(chunks).strip()
pathlib.Path(".llm_raw.txt").write_text(txt, encoding="utf-8")
