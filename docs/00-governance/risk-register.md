# Risk Register — GOV-03 v1.0-rc

Task: T-M0-007 · Last reviewed: 2026-10-20 (AG-01) · Next review: weekly report, week of 2026-10-26

Scoring: Likelihood (L) and Impact (I) are each rated 1–5; Score = L×I. Review weekly; any score ≥ 15 escalates to the Project Lead.

| ID | Risk | L | I | Score | Mitigation | Owner (agent / human) | Status |
|---|---|---|---|---|---|---|---|
| R-01 | Inaccurate or misattributed religious content shown to users | 2 | 5 | 10 | Approved-only catalogue, scholar gate, schema validator, no generative text | AG-08 Content Curator / Scholar | Open |
| R-02 | Google Play rejects the use of Usage Access or other sensitive permissions | 3 | 5 | 15 | Early policy spike (T-M2-002), prominent disclosure, fall-back to in-app EMA-only mode | AG-05 Platform Feasibility | Open |
| R-03 | Passive signals are too weak to infer feelings reliably | 4 | 4 | 16 | Treat inference as a prompt to check in, EMA-led labels, personal baselines, honest model cards | AG-06 ML Engineer | Open |
| R-04 | Privacy breach or perceived surveillance erodes trust | 2 | 5 | 10 | On-device processing, minimisation, threat model, transparency dashboard, delete-all | AG-09 Privacy & Security | Open |
| R-05 | User in crisis receives only a du'a, with no human help | 2 | 5 | 10 | Safety pathway SR-01..05, advisor review, helplines verified per region | AG-10 Safety Reviewer / MH advisor | Open |
| R-06 | Battery/performance drain causes uninstalls | 3 | 3 | 9 | WorkManager batching, budget tests (NFR-04) | AG-11 Android Dev | Open |
| R-07 | Insufficient labelled data for training | 4 | 4 | 16 | Public datasets for pre-study, pilot with EMA, active learning, transfer from population to personal models | AG-07 Data & Evaluation | Open |
| R-08 | Scholar reviewer bandwidth bottleneck | 3 | 4 | 12 | Small MVP catalogue (~60–100 items), batching, pre-validated metadata | Project Lead | Open |
| R-09 | AI agents invent requirements or bypass reviews | 3 | 4 | 12 | AGENTS.md, task IDs, AGS-04 matrix, process-only AG-00, handoff contracts, human gates, GitNexus impact before code edits, lean skill load | AG-00 Process Orchestrator | Open |
| R-10 | Licence conflict in open-source models/datasets/content | 2 | 4 | 8 | Licence register in the open-source AI evaluation | AG-04 Research Analyst | Open |
| R-11 | Ethics approval delays the pilot | 3 | 3 | 9 | Start the IRB application in M1 (T-M1-012) | Project Lead | Open |
| R-12 | Wrong recommendation timing feels intrusive (notification fatigue) | 3 | 3 | 9 | Rate limits, quiet hours, prayer-time awareness, user controls | AG-12 UX Designer | Open |
| R-13 | Bias: models perform worse for some user groups (age, gender, device) | 3 | 3 | 9 | Subgroup evaluation in model cards | AG-07 | Open |
| R-14 | Unreviewed or CI-failing changes land on `main` (no branch protection on private Free-plan repo, D-008) | 3 | 3 | 9 | PR template + task-ID CI check; work via PRs by convention; `check_repo.py` before merge; enable protection on upgrade to Pro or going public | Project Lead / Tech Lead | Open |
| R-15 | No mental-health advisor or scholar reviewer confirmed in time, blocking the safety pathway, content policy and G1 (A-04, A-08) | 3 | 5 | 15 | Recruit in T-M0-006; dated recruitment plan accepted at G0 if names are not confirmed | Project Lead | Open |
| R-16 | OEM battery optimisation kills background collection, leaving data gaps that weaken inference (A-09) | 4 | 3 | 12 | OEM spike T-M2-004 with gap rates; WorkManager; in-app guidance to exempt the app; gap-aware features in FS-001 | AG-05 Platform Feasibility | Open |

R-15 and R-16 scores are proposed by AG-01 for Project Lead confirmation.

## Escalations (score ≥ 15)
| Risk | Score | Escalated to | Date | Decision requested |
|---|---|---|---|---|
| R-02 | 15 | Project Lead | 2026-10-20 | Accept mitigation; go/no-go on Usage Access at G2 after T-M2-002 |
| R-03 | 16 | Project Lead | 2026-10-20 | Accept "ask, don't assert" framing and EMA-led labels; review after T-M2-006 |
| R-07 | 16 | Project Lead | 2026-10-20 | Accept public-dataset pre-study plus EMA pilot; review after T-M1-013 |
| R-15 | 15 | Project Lead | 2026-10-20 | Own T-M0-006 recruitment; confirm names or a dated plan before G0 (T-M0-009) |
