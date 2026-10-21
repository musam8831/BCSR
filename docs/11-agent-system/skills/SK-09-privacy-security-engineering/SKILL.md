---
name: privacy-security-engineering
description: Threat model, minimisation, encryption, Play Data Safety; Gate on privacy-impacting changes.
---

# SK-09 · privacy-security-engineering

**Version:** 0.3 · **Status:** Usable (bootstrap, T-M2-000) · validation in T-M3-006

## Purpose
Privacy-by-default and security fail-closed.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Update threat model for HC-01 change (`references/threat-checklist.md`).
2. Enforce on-device raw signals; no other-app content collection.
3. Review consent (PR-03) for any leave-device research path.
4. Gate Done when risks unmitigated.
5. Coordinate IRB/consent pack authorship as Collaborator with human gate.

## Outputs
- Privacy/security docs, threat model updates

## Quality checklist
- [ ] Threats mapped
- [ ] No prohibited observation
- [ ] Consent path clear if export

## Prohibitions
Do not approve AccessibilityService/IME observation. Do not weaken PR-03.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` §4 · ADR-0002 · threat-model · `references/threat-checklist.md`
