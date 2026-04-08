#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:-http://localhost:8080/plats-utilisateurs-service/api}"

check() {
  local path="$1"
  local expected="$2"

  local tmp
  tmp="$(mktemp)"
  local code
  code="$(curl -s -o "$tmp" -w '%{http_code}' "$BASE_URL$path")"

  echo "[$code] $BASE_URL$path"
  cat "$tmp"
  echo
  rm -f "$tmp"

  if [[ "$code" != "$expected" ]]; then
    echo "ERREUR: attendu HTTP $expected pour $path, obtenu $code" >&2
    exit 1
  fi
}

check "/health" "200"
check "/plats" "200"
check "/utilisateurs" "200"

echo "Smoke test OK."
