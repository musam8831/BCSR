---
name: requirements-engineering
description: Produce SRS, stories, ACs, and traceability; use for M1 baseline and CRs.
---

# SK · requirements-engineering

**Version:** 0.3 · **Status:** Usable (bootstrap, T-M1-001) · validation in T-M3-006

## Purpose
Complete, testable, traceable requirements within charter.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Read charter, scope, personas, research named in HC-01.
2. Assign Requirement IDs (FR-/NFR-/PR-/SR-/CR-) to every Must/Should.
3. Write stories and ACs; update traceability matrix.
4. Open HC-07 for gaps; never invent scope. A requested feature that conflicts with the charter's out-of-scope list or an ADR (e.g. paid LLM chat vs ADR-0003) is rejected with the citation, not drafted.
5. Align religious-content requirements to CR-* and scholar gate.
6. Submit HC-03 for AG-16 / Project Lead.

## Outputs
- `docs/01-requirements/*`

## Quality checklist
- [ ] Every Must has AC
- [ ] No out-of-charter requirements
- [ ] CR-* for religious content rules

## Prohibitions
Do not invent scope. Do not change baselined SRS without CR.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · `handoff-contracts.md` · `docs/01-requirements/SRS.md`
