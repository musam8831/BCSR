---
name: qa-testing
description: Test strategy, cases, automation, device matrix; M2–M10 QA.
---

# SK-14 · qa-testing

**Version:** 1.0 · **Status:** Validated (T-M3-008): edge and trap evals judged by an independent AG-16 (audits T-M3-006, T-M3-008)

## Purpose
Prove ACs with evidence on a realistic device matrix.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Map ACs to cases; include privacy, safety, content gates. Gate tests are never skipped or deferred "to save time"; a skipped gate test blocks Done.
2. Maintain matrix using `references/device-matrix-template.md`.
3. Automate where stable under `tests/`.
4. Link evidence for HC-03/HC-05.
5. Escalate gaps via HC-07.

## Outputs
- `docs/08-testing/`, `tests/`

## Quality checklist
- [ ] Must ACs covered
- [ ] Matrix current
- [ ] Evidence linked

## Prohibitions
Do not mark Done without evidence.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · acceptance-criteria · `references/device-matrix-template.md`
