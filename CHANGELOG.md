# Changelog

All notable changes follow [Keep a Changelog](https://keepachangelog.com/) conventions. Versions follow SemVer from the first app build onward; documentation baselines use `docs-vX.Y`.

## [Unreleased]

### Changed
- T-M0-007: Risk register GOV-03 v1.0-rc — reviewed; all risks owned; R-15 (advisor/scholar availability) and R-16 (OEM background gaps) added; escalations of R-02, R-03, R-07, R-15 (score ≥ 15) recorded. Decision log GOV-04 reviewed.
- T-M0-005: Scope & assumptions GOV-02 v1.0 — every assumption now cites a validating task ID; added A-08..A-12 (MH advisor, OEM background reliability, notification/activity access, IRB timing, pilot recruitment); taxonomy remains draft for T-M1-006; approved by Project Lead 2026-10-20.
- T-M0-004: Project charter GOV-01 v1.0 — objectives made SMART (deadline + verifier per objective, tied to milestones); signed by Project Lead 2026-10-20.
- T-M0-002: `AGENTS.md` v1.0 — version header, secrets rule (§4), branch/PR rule (§5.6, D-008), approval record (§8); approved by Project Lead 2026-10-20.
- T-M3-011 / D-007: Orchestration redesign — AGS-02 v0.2 (process orchestrator) + new AGS-04 Owner/Collaborator/Gate collaboration matrix; HC-01 gains Mode/O/C/G; `AGENTS.md` §5 and agent defs aligned; skills honour Mode/O/C/G from HC-01.
- T-M3-001..004 / T-M3-012: Expert skills SK-00..SK-16 deepened (v0.2 Draft) with role-specific procedures, non-clone evals, and reference packs for SK-06/07/08/09/10/11/12/13/14/15; agent defs AG-01..16 finalised; gap analysis confirms no new agents.

### Added
- T-M0-001: GitHub repository `musam8831/BCSR` (private) created; initial commit pushed to `main`; folder structure verified against README map (`check_repo.py` OK). Branch protection deferred per D-008 (Free plan, private repo); risk R-14.
- T-M3-012: Git repo init, GitNexus index for BCSR, lean `.cursor/rules/`, `bcsr-orchestrate` Cursor skill wrapper, `docs/11-agent-system/tooling-efficiency.md` (AGS-05).
- docs-v0.1: Initial M0–M3 execution package — governance, requirements catalogue, research briefs, architecture baseline, content policy, agent registry, 14 expert skills, milestones, task register and dependencies.
