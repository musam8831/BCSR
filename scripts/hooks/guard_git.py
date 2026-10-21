#!/usr/bin/env python3
"""Claude Code PreToolUse guard (D-008 compensating control, AGENTS.md §5.6).

Blocks: force-pushes, pushes to main, and `gh pr merge` unless every PR check passed.
Exit 2 = block (stderr is shown to the agent); exit 0 = allow.
"""
import json, re, shlex, subprocess, sys

def block(msg):
    print(f"Blocked by D-008 guard: {msg}", file=sys.stderr); sys.exit(2)

def current_branch():
    r = subprocess.run(["git", "branch", "--show-current"], capture_output=True, text=True)
    return r.stdout.strip()

cmd = json.load(sys.stdin).get("tool_input", {}).get("command", "")
for part in re.split(r"&&|\|\||;|\|", cmd):
    try:
        w = shlex.split(part)
    except ValueError:
        w = part.split()
    if w[:2] == ["git", "push"]:
        args = w[2:]
        if any(a in ("-f", "--force") or a.startswith("--force-with-lease") or a.startswith("+") for a in args):
            block("force-push is not allowed.")
        pos = [a for a in args if not a.startswith("-")]
        refs = pos[1:]
        if any(r == "main" or r.endswith(":main") or r.endswith(":refs/heads/main") for r in refs) \
                or (not refs and current_branch() == "main"):  # shortcut: branch is read before the command runs, so `git switch` earlier in the same command is missed (pre-push hook still blocks main); upgrade if agents chain switch+push
            block("pushing to main is not allowed; open a PR from a T-Mx-NNN branch.")
    if w[:3] == ["gh", "pr", "merge"]:
        nums = [a for a in w[3:] if re.fullmatch(r"\d+|https://\S+/pull/\d+", a)]
        r = subprocess.run(["gh", "pr", "checks", *nums[:1]], capture_output=True, text=True)
        if r.returncode != 0:
            block("PR checks are not all passing (or not reported yet):\n" + (r.stdout or r.stderr).strip())
sys.exit(0)
