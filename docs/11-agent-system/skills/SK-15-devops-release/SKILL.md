---
name: devops-release
description: CI/CD, signing, SBOM, Play tracks; enforce Task IDs and schema checks.
---

# SK-15 · devops-release

**Version:** 0.2 · **Status:** Draft (pilot validation in T-M3-006)

## Purpose
Pipelines block merges that lack Task IDs or ship unapproved content.

## Inputs
HC-01 task packet; documents it lists; `AGENTS.md`; AGS-04 Mode/O/C/G fields.

## Procedure
1. Maintain workflows under `.github/workflows/`.
2. Use `references/ci-workflow-templates.md` for Task-ID / docs-link / schema jobs.
3. Document release ops; secrets with AG-09.
4. Never bypass Release human gate.
5. Re-run GitNexus analyze after large infra/module drops.

## Outputs
- `.github/workflows/`, `docs/09-release-operations/`

## Quality checklist
- [ ] Blocking Task-ID/schema checks
- [ ] Signing/SBOM documented
- [ ] Release gate respected

## Prohibitions
Do not disable blocking governance checks. Do not bypass Release gate.

## Always
- Read `AGENTS.md` first. Work only on the task in your HC-01 packet. Honour `mode`, `owner`, `collaborators`, and `gates` from that packet (AGS-04).
- Cite Task IDs and Requirement IDs in every output.
- Submit using HC-03; never approve your own work.
- Never generate or alter Qur'an/Hadith text. Never use paid LLM services in the product.
- Prefer GitNexus `impact`/`query` before broad code reads when editing `app/`, `ml/`, or `scripts/` (see `docs/11-agent-system/tooling-efficiency.md`).

## References
`AGENTS.md` · release docs · `references/ci-workflow-templates.md`
