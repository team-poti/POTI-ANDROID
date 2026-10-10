#!/usr/bin/env bash
set -euo pipefail

VERSION_CODE="${1:?Version code is required}"
if [[ ! "$VERSION_CODE" =~ ^[1-9][0-9]*$ ]]; then
  echo "Invalid version code: $VERSION_CODE" >&2
  exit 1
fi

LATEST_CODE=0
LATEST_FILE=""
for NOTES_FILE in fastlane/metadata/android/ko-KR/changelogs/*.txt; do
  [ -f "$NOTES_FILE" ] || continue
  NOTES_CODE="${NOTES_FILE##*/}"
  NOTES_CODE="${NOTES_CODE%.txt}"
  [[ "$NOTES_CODE" =~ ^[1-9][0-9]*$ ]] || continue
  if [ "$NOTES_CODE" -le "$VERSION_CODE" ] && [ "$NOTES_CODE" -gt "$LATEST_CODE" ]; then
    LATEST_CODE="$NOTES_CODE"
    LATEST_FILE="$NOTES_FILE"
  fi
done

if [ -z "$LATEST_FILE" ]; then
  echo "No Korean release notes found for version code $VERSION_CODE or earlier." >&2
  exit 1
fi
NOTES=$(cat "$LATEST_FILE")
if [ -z "${NOTES//[[:space:]]/}" ] || [ "${#NOTES}" -gt 500 ]; then
  echo "Release notes must contain 1 to 500 characters: $LATEST_FILE" >&2
  exit 1
fi
printf '%s\n' "$LATEST_FILE"
