# Changelog

All notable changes follow [Keep a Changelog](https://keepachangelog.com/) conventions. Versions follow SemVer from the first app build onward; documentation baselines use `docs-vX.Y`.

## [Unreleased]

### Changed
- T-M1-013: Dataset strategy RES-04 v1.0-rc — instrument table with licence status (project-authored EMA items; PSS-10 + Urdu PSS-10 selected, two free permissions pending); no clinical screeners (D-011); public datasets checked (GLOBEM credentialed DUA, ExtraSensory citation-only, StudentLife unverified); shipped model trained only on BCSR-consented data; actions list. Approved by Tech Lead 2026-10-21 (PR #14; dual role, D-009).
- T-M1-002: SRS REQ-01 v0.2 — §1 Introduction and §2 Overall description written (ISO/IEC/IEEE 29148 structure); §3 module table gains inputs → outputs and requirement IDs for all 13 modules. No new requirements. Approved by Project Lead 2026-10-21 (PR #12, in place of AG-16).
- T-M1-001: SK-02, SK-04, SK-07, SK-08, SK-10, SK-12 bootstrapped to v0.3 (Usable) — 18/18 evals pass on desk run (7 after fixes: out-of-scope rejection, paywalled sources, catalogue-only content, duplicate refs, translation refusal + escalation, locale without helpline, no paraphrase in mocks). SK-10 now gates on the Project Lead (D-011). Approved by Project Lead 2026-10-21 (PR #11, in place of AG-16).
- T-M0-001: D-008 guard hook uses `$CLAUDE_PROJECT_DIR` so it works from any working directory.
- D-010: content follows the Ahl-e-Hadith manhaj (Qur'an and Sahih Hadith only); named ulama recorded as reference authorities. T-M0-006 stays In Progress until a living reviewer agrees.
- D-011: no mental-health advisor; Project Lead approves safety copy and pathway (SR-04, T-M1-006, T-M1-011, AG-10); safety pathway SR-01..05 unchanged; R-05 raised to 15 and accepted.
- T-M0-003: SK-00, SK-01, SK-16 bootstrapped to v0.3 (Usable) — 9/9 evals pass on desk run (4 after closing gaps: trap escalation in SK-00 and SK-16, same-day escalation in SK-01, broken-evidence rule in SK-16). Validation still in T-M3-006. Approved by Project Lead 2026-10-20, including the SK-00 step 6 reconciliation.
- T-M0-007: Risk register GOV-03 v1.0 — reviewed and approved by Project Lead 2026-10-20; all risks owned; R-15 (advisor/scholar availability) and R-16 (OEM background gaps) added; escalations of R-02, R-03, R-07, R-15 (score ≥ 15) recorded and accepted. Decision log GOV-04 v1.0 reviewed.
- T-M0-005: Scope & assumptions GOV-02 v1.0 — every assumption now cites a validating task ID; added A-08..A-12 (MH advisor, OEM background reliability, notification/activity access, IRB timing, pilot recruitment); taxonomy remains draft for T-M1-006; approved by Project Lead 2026-10-20.
- T-M0-004: Project charter GOV-01 v1.0 — objectives made SMART (deadline + verifier per objective, tied to milestones); signed by Project Lead 2026-10-20.
- T-M0-002: `AGENTS.md` v1.0 — version header, secrets rule (§4), branch/PR rule (§5.6, D-008), approval record (§8); approved by Project Lead 2026-10-20.
- T-M3-011 / D-007: Orchestration redesign — AGS-02 v0.2 (process orchestrator) + new AGS-04 Owner/Collaborator/Gate collaboration matrix; HC-01 gains Mode/O/C/G; `AGENTS.md` §5 and agent defs aligned; skills honour Mode/O/C/G from HC-01.
- T-M3-001..004 / T-M3-012: Expert skills SK-00..SK-16 deepened (v0.2 Draft) with role-specific procedures, non-clone evals, and reference packs for SK-06/07/08/09/10/11/12/13/14/15; agent defs AG-01..16 finalised; gap analysis confirms no new agents.

### Added
- T-M1-009: Licence register RES-03 v1.0-rc — 13 software components verified from their repositories (all Apache-2.0, MIT or BSD-3; no copyleft), Tanzil Quran text (CC BY 3.0, verbatim only), datasets and instruments cross-referenced to RES-04; no pre-trained models used.
- T-M1-008: Scientific evidence brief RES-02 v1.0-rc — 36 peer-reviewed sources, every DOI verified against OpenAlex; answers RQ1–RQ5 from abstracts with an evidence table and 8 design implications. Gaps: Urdu PSS-10 validation and instrument licences (T-M1-013). Approved by Project Lead 2026-10-21 (PR #13, in place of AG-07).
- T-M0-001 / D-008: Tech Lead accepts deferred branch protection with compensating controls — Claude Code guard hook + deny rules (`.claude/settings.json`, `scripts/hooks/guard_git.py`), git pre-push hook (`.githooks/pre-push`), CI audit that every `main` commit comes from a merged PR. D-008 expires before M4. T-M0-001 Done.
- D-009 / R-17: Muhammad Usman holds Project Lead and Tech Lead roles; charter GOV-01 v1.1 §6.
- T-M0-008: CI step `scripts/check_pr_dod.sh` fails non-draft PRs with a missing or incomplete Definition of Done (re-runs on PR edit); weekly report template `weekly-reports/TEMPLATE.md`. Approved by Project Lead 2026-10-20 (PR #7, in place of AG-16).
- T-M0-009: G0 gate pack (`gates/G0.md`); **G0 decided Go by Project Lead 2026-10-21**. T-M0-006 recruitment plan recorded (Dr. Hafiz Hasan Madni + IIUI Hadith dept; approach by 2026-10-31, confirm by 2026-11-15).
- T-M0-001: GitHub repository `musam8831/BCSR` (private) created; initial commit pushed to `main`; folder structure verified against README map (`check_repo.py` OK). Branch protection deferred per D-008 (Free plan, private repo); risk R-14.
- T-M3-012: Git repo init, GitNexus index for BCSR, lean `.cursor/rules/`, `bcsr-orchestrate` Cursor skill wrapper, `docs/11-agent-system/tooling-efficiency.md` (AGS-05).
- docs-v0.1: Initial M0–M3 execution package — governance, requirements catalogue, research briefs, architecture baseline, content policy, agent registry, 14 expert skills, milestones, task register and dependencies.
