---
name: traceability-audit
description: Independent audit vs requirements/DoD; HC-04/HC-05 verdicts.
---

# SK-16 · traceability-audit

**Version:** 0.3 · **Status:** Usable (bootstrap, T-M0-003) · validation in T-M3-006

## Purpose
Four-eyes assurance; trap violations never soft-pass.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Read HC-03; do not author the work under review.
2. Check Task/Requirement IDs, DoD, AGS-04 Gates, human-gate needs.
3. File HC-04 findings; issue HC-05 (`approve | approve-with-minors | changes-requested | reject`) to AG-00.
   - Any acceptance-criterion evidence that is missing or unreachable (e.g. a broken path) → `changes-requested`, never `approve`.
4. Scripture generated or altered by an agent, paid LLM product use, or a gate bypass → `reject` and escalate to AG-00 the same day.
5. Update traceability notes when matrix gaps found.

## Outputs
- HC-04, HC-05, matrix notes

## Quality checklist
- [ ] Independent of author
- [ ] Evidence checked
- [ ] Verdict explicit

## Prohibitions
Do not author under-review work. Do not soft-pass §2–§4 traps.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · handoff-contracts · traceability-matrix
