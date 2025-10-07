#!/usr/bin/env bash
set -euo pipefail

USER_PROMPT="${USER_PROMPT:-}"
BASE_BRANCH="${BASE_BRANCH:-main}"
MAX_FILES="${MAX_FILES:-50}"
MAX_TOTAL_KB="${MAX_TOTAL_KB:-800}"
MAX_FILE_KB="${MAX_FILE_KB:-120}"

if [[ -n "$USER_PROMPT" ]]; then
  PROMPT="$USER_PROMPT"
elif [[ -f ".github/llm_prompt.md" ]]; then
  PROMPT="$(cat .github/llm_prompt.md)"
else
  PROMPT=$'You are an expert engineer acting as a careful, minimal-change code improver.\n'\
         $'- Make small, safe improvements: correctness fixes, clearer names, missing edge checks, micro-perf wins, docstrings.\n'\
         $'- Preserve style and public APIs. Do not reformat entire files or introduce deps.\n'\
         $'- Keep changes incremental and self-contained.\n'
fi
printf "%s" "$PROMPT" > .llm_prompt.txt

IGNORE_FILE=".github/llm_ignore.txt"
ignore=()
if [[ -f "$IGNORE_FILE" ]]; then
  mapfile -t ignore < <(grep -v '^\s*$' "$IGNORE_FILE" | grep -v '^\s*#' || true)
fi

mapfile -t candidates < <(
  git ls-files \
    | grep -E '\.(py|js|ts|tsx|jsx|json|yml|yaml|md|go|rs|java|kt|kts|cs|cpp|cxx|cc|c|h|hpp|m|mm|rb|php|sh|bash|zsh|toml|ini|cfg|txt|sql|css|scss|vue|svelte|swift)$' \
    | grep -Ev '^(vendor/|dist/|build/|out/|.next/|node_modules/|target/|.venv/|venv/|.git/|coverage/|.tox/|__pycache__/|.pytest_cache/)'
)

if (( ${#ignore[@]} > 0 )); then
  filtered=()
  for f in "${candidates[@]}"; do
    skip=0
    for pat in "${ignore[@]}"; do
      [[ "$f" == $pat ]] && { skip=1; break; }
    done
    (( skip == 0 )) && filtered+=("$f")
  done
  candidates=("${filtered[@]}")
fi

: > .llm_files.txt
count=0
for f in "${candidates[@]}"; do
  echo "$f" >> .llm_files.txt
  ((count++))
  [[ "$count" -ge "$MAX_FILES" ]] && break
done

bytes_left=$(( MAX_TOTAL_KB * 1024 ))
cap_per=$(( MAX_FILE_KB * 1024 ))
: > .llm_context.txt

while IFS= read -r f; do
  [[ -f "$f" ]] || continue
  echo -e "\n===== FILE: $f =====\n" >> .llm_context.txt
  size=$(wc -c <"$f" || echo 0)
  if (( size > cap_per )); then
    head -c "$cap_per" "$f" >> .llm_context.txt
  else
    cat "$f" >> .llm_context.txt
  fi
  sz=$(wc -c < .llm_context.txt)
  if (( sz >= MAX_TOTAL_KB * 1024 )); then
    break
  fi
done < .llm_files.txt
