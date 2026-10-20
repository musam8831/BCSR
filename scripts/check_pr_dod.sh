#!/usr/bin/env bash
# Fails if the PR body ($BODY) has no "## Definition of Done" section or leaves any item unchecked.
dod=$(printf '%s\n' "$BODY" | tr -d '\r' | sed -n '/^## Definition of Done/,/^## /p')
[ -n "$dod" ] || { echo "No '## Definition of Done' section (use .github/pull_request_template.md)"; exit 1; }
open=$(printf '%s\n' "$dod" | grep -E '^- \[ \]')
[ -z "$open" ] || { echo "Unchecked Definition of Done items:"; echo "$open"; exit 1; }
echo "Definition of Done complete"
