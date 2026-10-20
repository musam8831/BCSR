# Agent tooling efficiency — AGS-05 v0.1

> **Task:** T-M3-012  
> **Goal:** Minimum sufficient context per turn; GitNexus + lean Cursor rules; skills stay under ~200 lines.

## 1. Load order (every session)

1. `AGENTS.md` (always; keep lean — do not bloat).
2. HC-01 packet fields: `mode`, `owner`, `collaborators`, `gates`, `skills`, cited paths.
3. One skill: `docs/11-agent-system/skills/SK-NN-*/SKILL.md`.
4. `references/` inside that skill only when the procedure requires it.
5. For code edits: GitNexus `context` → `impact` / `query` before broad reads.
6. Glob-scoped `.cursor/rules/` (android / ml / content-safety) apply when matching files are in play.

## 2. MCP / connectors

| Connector | Scope | Use |
|---|---|---|
| GitNexus (user `mcp.json`) | Graph index | Primary code navigation; token-saving vs dumping trees |
| Browser / Slack / Linear | Not default | Enable only for a specific task |
| GitHub plugin | When remote/PRs exist | Re-auth if discovery errors; not required for local docs work |

Do **not** add project `.cursor/mcp.json` unless overrides differ from user config. User-level GitNexus is enough.

## 3. GitNexus commands

```powershell
cd C:\xampp-updated\htdocs\BCSR
gitnexus analyze
gitnexus status
```

Index lives in `.gitnexus/` (gitignored). Re-analyze after large code or module drops. Prefer `impact` before symbol edits.

**AGENTS.md protection:** GitNexus must not replace project operating rules. Navigation tips stay in `.cursor/rules/gitnexus-usage.mdc` and this file.

## 4. Cursor project skills

| Path | Role |
|---|---|
| `.cursor/skills/bcsr-orchestrate/` | Thin routing wrapper → AGS-04 / AG-00 |
| `docs/11-agent-system/skills/SK-*` | Source of truth for domain expertise |

Do not duplicate all 17 skills into `.cursor/skills/`.

## 5. Token anti-patterns

- Pasting entire `docs/` folders into prompts
- Loading every SKILL.md for one task
- Always-on rules that embed long policy text already in `AGENTS.md`
- Enabling unused MCP servers that inject large schemas each turn
