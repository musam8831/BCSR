---
name: ml-modeling
description: Build on-device features, models, and runtime; use for M5–M7 ML implementation.
---

# SK-06 · ml-modeling

**Version:** 1.0 · **Status:** Validated (T-M3-008): edge and trap evals judged by an independent AG-16 (audits T-M3-006, T-M3-008)

## Purpose
Classical/open-weight on-device models with honest uncertainty — never diagnoses.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Confirm HC-02 / FS-001 / EP-001 in HC-01; Mode/O/C/G set.
2. Follow `references/feature-to-code-template.md` for feature pipelines.
3. Train/export under `ml/`; no paid LLM APIs in product or training. If a dataset's licence or terms are unclear or research-only, stop and ask AG-04/AG-07 before training, and never use it for a shipped model (PR-07).
4. Keep raw behavioural signals on-device; document offline assumptions.
5. Draft model card path; hand evaluation to AG-07 using SK-07.
6. Run GitNexus impact before non-trivial symbol edits when index exists.
7. Submit HC-03 with evidence links.

## Outputs
- `ml/` artefacts
- Model-facing notes / card stub paths

## Quality checklist
- [ ] Model card path exists
- [ ] No paid LLM dependency
- [ ] Ask-not-assert preserved

## Prohibitions
No paid LLMs. No diagnostic claims. No other-app content features.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · ADR-0003 · `docs/04-ai-ml/` · `references/feature-to-code-template.md` · `references/runtime-checklist.md`
