---
name: research-synthesis
description: Synthesise evidence and open-source/licence evaluation for M1–M2 research.
---

# SK · research-synthesis

**Version:** 1.0 · **Status:** Validated (T-M3-008): edge and trap evals judged by an independent AG-16 (audits T-M3-006, T-M3-008)

## Purpose
Ground choices in citations and compatible licences.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Gather sources from HC-01; prefer primary/peer-reviewed.
2. Separate claim vs inference in briefs. A source you cannot access (e.g. paywalled) is not cited as read: record the claim as an assumption and propose a validating task.
3. Update open-source AI evaluation and licence notes; flag R-10 conflicts.
4. Hand ML findings to AG-07; architecture implications to AG-03.

## Outputs
- Research briefs under `docs/02-research/`

## Quality checklist
- [ ] Claims cited
- [ ] Licence conflicts explicit
- [ ] No invented results

## Prohibitions
Do not fabricate citations or study outcomes. Never place Qur'an/Hadith text or translations in briefs as app content; app content enters only via `content/catalogue/` (AGENTS.md §2).

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · `docs/02-research/`
