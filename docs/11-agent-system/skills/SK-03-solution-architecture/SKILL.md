---
name: solution-architecture
description: Produce C4 views, ADRs, API contracts, module boundaries; resolve Owner boundary disputes.
---

# SK · solution-architecture

**Version:** 1.0 · **Status:** Validated (T-M3-008): edge and trap evals judged by an independent AG-16 (audits T-M3-006, T-M3-008)

## Purpose
Architecture stays feasible, private, and ADR-aligned.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Read SRS and spikes in HC-01.
2. Update C4/component/data-flow; keep AGS-04 path Owners coherent. If two Owners claim the same path, stop, escalate to AG-00 and record the boundary in an ADR; never pick silently.
3. Draft ADRs; link decision-log.
4. Define API contracts and data dictionary deltas.
5. Fail-closed on ADR-0002/0003 violations.
6. On cross-domain epic: advise AG-00 sequencing; document boundary decision.

## Outputs
- `docs/03-architecture/*`

## Quality checklist
- [ ] ADRs have status/consequences
- [ ] NFRs addressed
- [ ] No prohibited observation or paid LLM product paths

## Prohibitions
Do not approve insecure observation. Do not override the Scholar or Project Lead safety gates (D-011 replaces the MH gate).

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · `docs/03-architecture/` · `agent-collaboration.md`
