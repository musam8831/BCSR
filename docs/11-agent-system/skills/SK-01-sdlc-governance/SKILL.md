---
name: sdlc-governance
description: Produce and maintain governance artefacts and gate packs; support AG-00 Mode/O/C/G selection.
---

# SK-01 · sdlc-governance

**Version:** 0.2 · **Status:** Draft (pilot validation in T-M3-006)

## Purpose
Keep scope, risks, decisions, and gates controlled and evidence-linked.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. When supporting orchestration: read AGS-04; select Mode/Owner/C/G; record considered-but-not-needed always-on items; emit HC-01 fields.
2. Draft or update charter/scope from Project Lead input; mark unknowns as assumptions with validating tasks.
3. Maintain risk register weekly (L×I); escalate scores ≥ 15.
4. Record decisions in decision-log.md; ADR stubs to AG-03 when architectural.
5. Assemble gate packs with exit criteria, evidence links, open risks, go/no-go/conditional.
6. Run milestone retrospectives; feed skill improvements to SK-00.
7. Never bypass G0–G3, Scholar, MH, IRB, or Release human gates.

## Outputs
- `docs/00-governance/*`
- Gate packs under `docs/10-project-management/gates/`

## Quality checklist
- [ ] Objectives measurable
- [ ] Risks owned with mitigations
- [ ] Gate pack links evidence

## Prohibitions
Do not invent scope. Do not approve your own gate recommendation as final human approval.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · `handoff-contracts.md` · `agent-collaboration.md` · `orchestration-policy.md` · `SRS.md`
