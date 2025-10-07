#!/usr/bin/env bash
set -euo pipefail

USER_PROMPT="${USER_PROMPT:-}"
BASE_BRANCH="${BASE_BRANCH:-main}"
MAX_FILES="${MAX_FILES:-50}"
MAX_TOTAL_KB="${MAX_TOTAL_KB:-800}"
MAX_FILE_KB="${MAX_FILE_KB:-120}"

# 1) Build effective prompt -> write straight to file (avoid multi-line bash strings)
if [[ -n "$USER_PROMPT" ]]; then
  printf "%s" "$USER_PROMPT" > .llm_prompt.txt
elif [[ -f ".github/llm_prompt.md" ]]; then
  cat .github/llm_prompt.md > .llm_prompt.txt
else
  cat > .llm_prompt.txt <<'PROMPT'
You are an expert engineer acting as a careful, minimal-change code improver.
- Make small, safe improvements: correctness fixes, clearer names, missing edge checks, micro-perf wins, docstrings.
- Preserve style and public APIs. Do not reformat entire files or introduce new dependencies.
- Keep changes incremental and self-contained.
PROMPT
fi

# 2) Optional ignore patterns
IGNORE_FILE=".github/llm_ignore.txt"
ignore=()
if [[ -f "$IGNORE_FILE" ]]; then
  # ignore blank lines and comments
  mapfile -t ignore < <(grep -v '^\s*$' "$IGNORE_FILE" | grep -v '^\s*#' || true)
fi

# 3) Candidate files (tolerate 0 matches)
# NOTE: the final '|| true' prevents pipefail from killing the script when grep finds no matches
mapfile -t candidates < <(
  git ls-files \
    | grep -E '\.(py|js|ts|tsx|jsx|json|yml|yaml|md|go|rs|java|kt|kts|cs|cpp|cxx|cc|c|h|hpp|m|mm|rb|php|sh|bash|zsh|toml|ini|cfg|txt|sql|css|scss|vue|svelte|swift)$' \
    | grep -Ev '^(vendor/|dist/|build/|out/|\.next/|node_modules/|target/|\.venv/|venv/|\.git/|coverage/|\.tox/|__pycache__/|\.pytest_cache/)' \
    || true
)

# 4) Apply ignore list (still fine if 'candidates' is empty)
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

# 5) Save bounded file list (always create the file)
: > .llm_files.txt
count=0
for f in "${candidates[@]}"; do
  echo "$f" >> .llm_files.txt
  ((count++))
  [[ "$count" -ge "$MAX_FILES" ]] && break
done

# 6) Build truncated context (always create the file)
cap_per=$(( MAX_FILE_KB * 1024 ))
: > .llm_context.txt

while IFS= read -r f; do
  [[ -f "$f" ]] || continue
  {
    echo
    echo "===== FILE: $f ====="
    echo
  } >> .llm_context.txt
  size=$(wc -c <"$f" || echo 0)
  if (( size > cap_per )); then
    head -c "$cap_per" "$f" >> .llm_context.txt
  else
    cat "$f" >> .llm_context.txt
  fi
  current=$(wc -c < .llm_context.txt)
  (( current >= MAX_TOTAL_KB * 1024 )) && break
done < .llm_files.txt

# 7) Friendly log (does not affect exit status)
echo "Prepared prompt (.llm_prompt.txt), file list ($(wc -l < .llm_files.txt) files), and context ($(wc -c < .llm_context.txt) bytes)."
