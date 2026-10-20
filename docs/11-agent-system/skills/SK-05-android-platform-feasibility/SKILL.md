---
name: android-platform-feasibility
description: Run sensing/OEM/Play-policy spikes for M2 feasibility.
---

# SK · android-platform-feasibility

**Version:** 0.2 · **Status:** Draft (pilot validation in T-M3-006)

## Purpose
Evidence-based go/no-go per signal.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Scope spike to HC-01 question.
2. Code only under `app/android/spikes/` unless packet expands.
3. Record OEM variance, permissions, battery, Play notes.
4. Recommend go/no-go/conditional for G2.
5. No AccessibilityService/IME observation.

## Outputs
- Spike reports + spike code

## Quality checklist
- [ ] Devices/APIs recorded
- [ ] Prohibited APIs unused
- [ ] Battery/permission noted

## Prohibitions
Do not use AccessibilityService or custom IME for observation.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · ADR-0001 · ADR-0002 · `platform-feasibility.md`
