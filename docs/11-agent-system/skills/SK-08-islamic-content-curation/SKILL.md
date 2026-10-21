---
name: islamic-content-curation
description: Draft catalogue metadata, validate schema, prepare scholar HC-06 packs; never generate religious text.
---

# SK-08 · islamic-content-curation

**Version:** 0.3 · **Status:** Usable (bootstrap, T-M1-001; approved by Project Lead 2026-10-21, PR #11) · validation in T-M3-006

## Purpose
Curate verified catalogue entries for scholar approval only.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Validate against schema (`references/schema-validator-usage.md`).
2. Draft metadata/tags/contexts only — never verse/Hadith text generation.
3. Prepare HC-06 using `references/hc06-pack-template.md`.
4. Reject da'if/mawdu'; require Sahih grading source.
   - Check new items against existing references; flag a duplicate in the HC-06 pack and never create new text to make it differ.
5. Leave `verification.status` for Scholar; agents never set `approved`.

## Outputs
- Catalogue metadata
- HC-06 packs
- docs/05-spiritual-content updates

## Quality checklist
- [ ] Schema valid
- [ ] No agent-authored scripture
- [ ] Scholar gate intact

## Prohibitions
Never generate/paraphrase/translate/complete scripture, including "improving" a translation: refuse, cite AGENTS.md §2, escalate to AG-00. Never self-approve content.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` §2 · ADR-0004 · `docs/05-spiritual-content/` · `references/*`
