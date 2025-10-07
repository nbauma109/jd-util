#!/usr/bin/env bash
# Verbose + safe
set -Eeuo pipefail
trap 'echo "[ERROR] line $LINENO: $BASH_COMMAND" >&2' ERR

log() { echo "[$(date -u +%H:%M:%S)] $*"; }

log "---- llm_prep.sh starting ----"

# ---------- Inputs ----------
USER_PROMPT="${USER_PROMPT:-}"
BASE_BRANCH="${BASE_BRANCH:-main}"
MAX_FILES="${MAX_FILES:-50}"
MAX_TOTAL_KB="${MAX_TOTAL_KB:-800}"
MAX_FILE_KB="${MAX_FILE_KB:-120}"

log "ENV:"
log "  BASE_BRANCH=${BASE_BRANCH}"
log "  MAX_FILES=${MAX_FILES}"
log "  MAX_FILE_KB=${MAX_FILE_KB}"
log "  MAX_TOTAL_KB=${MAX_TOTAL_KB}"
log "  USER_PROMPT len=$(printf %s "$USER_PROMPT" | wc -c || echo 0)"

# ---------- Prompt ----------
if [[ -n "$USER_PROMPT" ]]; then
  log "Using USER_PROMPT input"
  printf "%s" "$USER_PROMPT" > .llm_prompt.txt
elif [[ -f ".github/llm_prompt.md" ]]; then
  log "Using .github/llm_prompt.md"
  cat .github/llm_prompt.md > .llm_prompt.txt
else
  log "Using built-in default prompt"
  cat > .llm_prompt.txt <<'PROMPT'
You are an expert engineer acting as a careful, minimal-change code improver.
- Make small, safe improvements: correctness fixes, clearer names, missing edge checks, micro-perf wins, docstrings.
- Preserve style and public APIs. Do not reformat entire files or introduce new dependencies.
- Keep changes incremental and self-contained.
PROMPT
fi
log "Wrote .llm_prompt.txt (bytes=$(wc -c < .llm_prompt.txt))"

# ---------- Ignore list ----------
IGNORE_FILE=".github/llm_ignore.txt"
ignore_raw=()
if [[ -f "$IGNORE_FILE" ]]; then
  log "Reading ignore patterns from $IGNORE_FILE"
  mapfile -t ignore_raw < <(grep -v '^\s*$' "$IGNORE_FILE" | grep -v '^\s*#' || true)
else
  log "No $IGNORE_FILE found (this is fine)"
fi

# Normalize ignores to glob prefixes (e.g., "target/" -> "target/**")
ignore=()
for pat in "${ignore_raw[@]:-}"; do
  norm="$pat"
  if [[ "$norm" != *"*"* && "$norm" != *"?"* && "$norm" != *"["* ]]; then
    norm="${norm%/}/**"
  elif [[ "$norm" == */ ]]; then
    norm="${norm}**"
  fi
  ignore+=("$norm")
done

if ((${#ignore[@]} > 0)); then
  log "Normalized ignore patterns:"
  for p in "${ignore[@]}"; do log "  - $p"; done
else
  log "No ignore patterns in effect"
fi

# ---------- Candidate files ----------
log "Scanning repo for candidate files…"
mapfile -t candidates < <(
  git ls-files \
    | grep -E '\.(py|js|ts|tsx|jsx|json|yml|yaml|md|go|rs|java|kt|kts|cs|cpp|cxx|cc|c|h|hpp|m|mm|rb|php|sh|bash|zsh|toml|ini|cfg|txt|sql|css|scss|vue|svelte|swift)$' \
    | grep -Ev '^(vendor/|dist/|build/|out/|\.next/|node_modules/|target/|\.venv/|venv/|\.git/|coverage/|\.tox/|__pycache__/|\.pytest_cache/)' \
    || true
)
log "Initial candidates: ${#candidates[@]}"

# ---------- Apply ignore patterns ----------
if ((${#ignore[@]} > 0 && ${#candidates[@]} > 0)); then
  shopt -s dotglob nullglob globstar
  filtered=()
  for f in "${candidates[@]}"; do
    skip=0
    for pat in "${ignore[@]}"; do
      if [[ "$f" == $pat ]]; then
        skip=1
        break
      fi
    done
    if (( skip == 0 )); then
      filtered+=("$f")
    fi
  done
  candidates=("${filtered[@]}")
  log "After ignores: ${#candidates[@]}"
fi

# ---------- Bounded list ----------
: > .llm_files.txt
count=0
for f in "${candidates[@]:-}"; do
  echo "$f" >> .llm_files.txt
  count=$((count + 1))   # portable increment
  if (( count >= MAX_FILES )); then
    break
  fi
done
log "Selected files written to .llm_files.txt (count=$(wc -l < .llm_files.txt))"

# ---------- Build truncated context ----------
cap_per=$(( MAX_FILE_KB * 1024 ))
max_total=$(( MAX_TOTAL_KB * 1024 ))
: > .llm_context.txt

total=0
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
    added=$cap_per
  else
    cat "$f" >> .llm_context.txt
    added=$size
  fi

  total=$(( total + added + 16 ))
  if (( total >= max_total )); then
    log "Reached MAX_TOTAL_KB budget (${MAX_TOTAL_KB}KB); stopping context build"
    break
  fi
done < .llm_files.txt

log "Built .llm_context.txt (bytes=$(wc -c < .llm_context.txt))"
log "Prep complete."
log "---- llm_prep.sh done ----"
