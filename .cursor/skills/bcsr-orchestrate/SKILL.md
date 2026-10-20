---
name: bcsr-orchestrate
description: >-
  Route BCSR multi-agent work: which Owner, Mode, collaborators, gates.
  Use when Owner is unclear, user asks to orchestrate, plan multi-agent work,
  or assign HC-01 packets. Do not use for domain design or product code ownership.
---

# BCSR orchestrate (thin wrapper)

Process authority only. Full procedure: `docs/11-agent-system/agents/AG-00.md` and `docs/11-agent-system/orchestration-policy.md` (AGS-02).

## Steps

1. Read `AGENTS.md` (non-negotiables).
2. Select Mode / Owner / Collaborators / Gates / Informed from `docs/11-agent-system/agent-collaboration.md` (AGS-04).
3. Record always-on checklist items considered-but-not-needed.
4. Emit HC-01 field block(s) from `docs/11-agent-system/handoff-contracts.md`.
5. If Owner is unclear: output Mode/O/C/G/I/X and **stop** unless asked to continue as process orchestrator.
6. Point the next agent at exactly one skill path under `docs/11-agent-system/skills/` — do not load all skills.

Token rule: do not paste large docs; cite paths.
