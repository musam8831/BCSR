---
name: safety-wellbeing-review
description: Safety pathway, tone, helplines; HC-06 packs for the Project Lead (no MH advisor, D-011).
---

# SK-10 · safety-wellbeing-review

**Version:** 0.3 · **Status:** Usable (bootstrap, T-M1-001) · validation in T-M3-006

## Purpose
Crisis indicators trigger human-help pathways, not du'a-only.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Review UX/copy for SR-01..SR-05 (`references/safety-checklist.md`).
2. Update helpline directory with verified regional sources.
3. Prepare HC-06 for the Project Lead when safety content changes (D-011 replaces the MH advisor).
4. Fail-closed if crisis path missing, including a locale with no verified helpline: block ship for that locale and escalate to the Project Lead.

## Outputs
- Safety docs, HC-06 packs

## Quality checklist
- [ ] Crisis path present
- [ ] Ask-not-assert tone
- [ ] Project Lead safety gate intact (D-011)

## Prohibitions
Do not ship du'a-only crisis handling. Do not bypass the Project Lead safety gate.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` §4 · safety docs · `references/safety-checklist.md`
