---
name: android-development
description: Implement Kotlin/Compose modules under approved specs; M5–M8 app work.
---

# SK-11 · android-development

**Version:** 0.2 · **Status:** Draft (pilot validation in T-M3-006)

## Purpose
Ship Android features meeting ACs, NFRs, and privacy constraints.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Confirm HC-02 and Mode/O/C/G before coding.
2. Follow `references/kotlin-compose-conventions.md` and `references/module-layout.md`.
3. Implement under `app/android/`; single-writer respect.
4. No AccessibilityService/IME observation; no paid LLM in product.
5. GitNexus impact before large refactors when indexed.
6. Coordinate tests with AG-14; HC-03 submit.

## Outputs
- `app/android/` changes + linked tests

## Quality checklist
- [ ] ACs evidenced
- [ ] Budgets considered
- [ ] No prohibited APIs

## Prohibitions
No prohibited observation APIs. No paid LLM product calls.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · ADR-0001/0002 · `references/*`
