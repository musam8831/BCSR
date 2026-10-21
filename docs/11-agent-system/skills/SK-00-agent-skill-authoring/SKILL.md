---
name: agent-skill-authoring
description: Create or revise BCSR agent definitions and SKILL.md expert skills; use for M3 skill work or post-retro improvements.
---

# SK-00 · agent-skill-authoring

**Version:** 1.0 · **Status:** Validated (T-M3-008): edge and trap evals judged by an independent AG-16 (audits T-M3-006, T-M3-008)

## Purpose
Design agents and skills that are narrow, testable, safe, and token-lean (<~200 lines + references/).

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Read agent-registry.md, orchestration-policy.md (AGS-02), agent-collaboration.md (AGS-04), tooling-efficiency.md, and target milestone tasks.
2. Confirm gap-analysis.md: do not invent AG-17+ without documented reopen triggers.
3. Define or revise the agent file: mission, collab_role, inputs, outputs, may_write, must_not, reviewers, handoffs.
4. Write SKILL.md: YAML name/description (when to use), Purpose, Inputs, Procedure, Outputs, Quality checklist, Prohibitions, References.
5. Keep SKILL.md under ~200 lines; put long templates in `references/`.
6. Write three role-specific evals (normal / edge / trap). The author may record a desk-run result, but a desk run is not validation: v1.0 needs the edge and trap evals judged by an independent AG-16 (M0–M3 retrospective). Claims in HC-03 must point at an artefact that exists.
7. Align path ownership with AGS-04; register version notes in CHANGELOG when user-visible.
8. Point Cursor wrappers (if any) at docs skills — never duplicate full bodies into `.cursor/skills/`.

## Outputs
- Updated `SKILL.md`, `evals.md`, optional `references/`
- Agent file and/or registry row when needed

## Quality checklist
- [ ] Description states trigger conditions
- [ ] Procedure executable without hidden knowledge
- [ ] Prohibitions cover AGENTS.md §2–§4 for the role
- [ ] Evals are non-clone and include a trap
- [ ] SKILL.md < ~200 lines

## Prohibitions
Do not mark a skill v1.0 without an independent AG-16 edge and trap verdict. Do not weaken human gates. Do not bloat AGENTS.md.
Reject any agent or skill text that would permit generating, paraphrasing or translating Qur'an/Hadith, paid LLMs in the product, or a gate bypass: cite AGENTS.md §2–§4 and escalate to AG-00.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · `agent-registry.md` · `agent-collaboration.md` · `orchestration-policy.md` · `tooling-efficiency.md` · `handoff-contracts.md` · `gap-analysis.md`
