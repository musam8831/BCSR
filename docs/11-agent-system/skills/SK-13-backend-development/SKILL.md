---
name: backend-development
description: Optional research ingestion and catalogue signing pipelines; M4–M5.
---

# SK-13 · backend-development

**Version:** 0.3 · **Status:** Usable (T-M3-006 pilot) · validation in T-M3-006

## Purpose
Minimal backend/scripts preserving privacy and catalogue integrity.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Implement only HC-01/spec scope under `app/backend/` or `scripts/`.
2. Follow `references/pipeline-template.md`.
3. Never accept raw behavioural PII; research export needs consent path.
4. Reject unapproved `verification.status` in signing. An item is "unsigned" if it is not `approved` with a review record. If any item in a batch fails validation, fail the whole batch, build nothing, and report every failing item ID (cf. `scripts/build_catalogue_bundle.py`).
5. Security review with AG-09; HC-03.

## Outputs
- `app/backend/`, `scripts/`

## Quality checklist
- [ ] No raw signal exfiltration
- [ ] Unapproved items rejected
- [ ] AG-09 addressed

## Prohibitions
No raw PII pipelines. No bypass of catalogue approval.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · content validation · `references/pipeline-template.md`
