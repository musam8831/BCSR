---
name: ml-evaluation
description: Own evaluation plans, model cards, bias checks; use for M1–M7 evaluation.
---

# SK-07 · ml-evaluation

**Version:** 0.3 · **Status:** Usable (bootstrap, T-M1-001) · validation in T-M3-006

## Purpose
Honest metrics, subgroups, and limitations before merge.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Align EP-001 and dataset strategy with requirements and ethics.
2. Define metrics, splits, baselines, subgroup checks (`references/eval-script-stub.md`).
3. Review/co-author model cards (`references/model-card-checklist.md`).
4. Block ship language that diagnoses or overclaims.
5. Coordinate with AG-06 on experiment registry entries.

## Outputs
- Evaluation plans, model cards, `ml/evaluation/`

## Quality checklist
- [ ] Metrics match ACs
- [ ] Subgroup checks listed
- [ ] Limitations explicit

## Prohibitions
Do not hide failed metrics. Do not approve diagnose-style UX copy.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · `docs/04-ai-ml/` · `references/*`
