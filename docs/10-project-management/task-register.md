# Task Register — PM-02 v0.2 (M0–M5 prioritised backlog)

Owner: AG-00. Status values: Backlog · Ready · In Progress · In Review · Done · Blocked. **Ready** = HC-01 packet complete (`packets/M4-M5.md`), no open questions, and nothing in its dependency chain waits on a person or device outside M4/M5; it starts once its listed dependencies (including G3, T-M3-010) are Done. Priority: P1 = critical path, P2 = important, P3 = nice to have. A machine-readable copy is in `task-register.csv` (import it into any tracker).

| ID | Task | Agent | Skills | Priority | Est. days | Depends on | Outputs | Acceptance | Reviewer | Status |
|---|---|---|---|---|---|---|---|---|---|---|
| **M0** | | | | | | | | | | |
| T-M0-001 | Create repository, branch protection, folder structure | AG-15/Human | SK-15 | P1 | 1 |  | Repo per README map | main protected; structure matches README | Tech Lead | Done |
| T-M0-002 | Finalise AGENTS.md operating rules | AG-01 | SK-01 | P1 | 1 | T-M0-001 | AGENTS.md | Rules cover content, AI, privacy, gates; Lead approves | Project Lead | Done |
| T-M0-003 | Bootstrap SK-00, SK-01, SK-16 skills (v0.1 → usable) | AG-00 | SK-00 | P1 | 2 | T-M0-002 | skills/SK-00, SK-01, SK-16 | Each passes its 3 evals | AG-16 + Lead | Done |
| T-M0-004 | Finalise project charter and objectives | AG-01 | SK-01 | P1 | 2 | T-M0-002 | 00-governance/project-charter.md | SMART objectives; Lead signs | Project Lead | Done |
| T-M0-005 | Finalise scope, context-state taxonomy draft, assumptions | AG-01 | SK-01 | P1 | 2 | T-M0-004 | scope-and-assumptions.md | Each assumption has a validating task | AG-16 | Done |
| T-M0-006 | Identify and confirm scholar reviewer(s) and MH advisor | Human (Lead) | — | P1 | 5 | T-M0-004 | stakeholder table in charter | Named people with agreed availability | Project Lead | In Progress |
| T-M0-007 | Initial risk register and decision log | AG-01 | SK-01 | P1 | 1 | T-M0-004 | risk-register.md, decision-log.md | All risks owned; scores ≥ 15 escalated | AG-16 | Done |
| T-M0-008 | Create task register, PR template, weekly report template | AG-00 | SK-01 | P1 | 1 | T-M0-001 | task-register.md, .github/pull_request_template.md | Template enforces task ID + DoD | AG-16 | Done |
| T-M0-009 | G0 gate pack and review | AG-01 | SK-01 | P1 | 1 | T-M0-004;T-M0-005;T-M0-006;T-M0-007 | gates/G0.md | Lead decision recorded | Project Lead | Done |
| **M1** | | | | | | | | | | |
| T-M1-001 | Bootstrap SK-02, SK-04, SK-07, SK-08, SK-10, SK-12 skills for M1 | AG-00 | SK-00 | P1 | 3 | T-M0-009 | skills folders | Evals pass incl. trap cases | AG-16 | Done |
| T-M1-002 | SRS sections 1–2 and module descriptions | AG-02 | SK-02 | P1 | 3 | T-M1-001 | SRS.md | 29148 structure; reviewed | AG-16 | Done |
| T-M1-003 | User interviews (≥ 8) and persona validation | AG-12 + Human | SK-12 | P1 | 8 | T-M1-001 | personas.md, interview notes (anonymised) | ≥ 8 interviews; personas updated | Project Lead | In Progress |
| T-M1-004 | Complete requirements catalogue (FR/NFR/PR/SR/CR) | AG-02 | SK-02 | P1 | 5 | T-M1-002;T-M1-003 | SRS.md §5 | All testable; MoSCoW set | AG-16 | In Review |
| T-M1-005 | Acceptance criteria for all Must requirements | AG-02 | SK-02 | P1 | 4 | T-M1-004 | acceptance-criteria.md | 100% Musts covered | AG-14 + AG-16 | Done |
| T-M1-006 | Validate context-state taxonomy with evidence + advisor | AG-07 | SK-07 | P1 | 3 | T-M1-008 | scope-and-assumptions.md §1 | Project Lead and scholar comments resolved (D-011) | Project Lead | In Review |
| T-M1-007 | Full traceability matrix | AG-02 | SK-02;SK-16 | P1 | 2 | T-M1-005 | traceability-matrix.md | Every Req has Story+AC rows | AG-16 | Done |
| T-M1-008 | Scientific evidence brief (RQ1–RQ5) | AG-04 | SK-04 | P1 | 8 | T-M1-001 | scientific-evidence.md | ≥ 25 cited sources; evidence table | AG-07 | Done |
| T-M1-009 | Open-source AI evaluation & licence register | AG-04 | SK-04 | P1 | 4 | T-M1-001 | open-source-ai-evaluation.md | All candidates' licences verified | Tech Lead | Done |
| T-M1-010 | Content source policy, schema, verification workflow; approve with scholar | AG-08 | SK-08 | P1 | 5 | T-M0-006;T-M1-001 | 05-spiritual-content/* | Scholar signs the policy | Scholar | In Review |
| T-M1-011 | Safety pathway v1 and helpline directory (verified) | AG-10 | SK-10 | P1 | 4 | T-M0-006;T-M1-001 | 06-security-privacy-safety/safety/* | Project Lead signs (D-011); helplines verified with date | Project Lead | In Review |
| T-M1-012 | Ethics/IRB application for the pilot study | Human (Lead) + AG-07 | SK-07 | P1 | 5 | T-M1-013 | ethics application | Submitted | Project Lead | In Progress |
| T-M1-013 | Dataset strategy and EMA instrument selection | AG-07 | SK-07 | P1 | 3 | T-M1-008 | dataset-strategy.md | Licensed instruments; split rules | Tech Lead | Done |
| T-M1-014 | G1 gate pack and review | AG-01 | SK-01 | P1 | 1 | T-M1-007;T-M1-010;T-M1-011;T-M1-012 | gates/G1.md | Baseline frozen | Lead + Domain reviewers | In Progress |
| **M2** | | | | | | | | | | |
| T-M2-000 | Bootstrap SK-03, SK-05, SK-06, SK-09, SK-11, SK-14 skills for M2 | AG-00 | SK-00 | P1 | 3 | T-M1-014 | skills folders | Evals pass | AG-16 | Done |
| T-M2-001 | Spike: UsageStats events & screen/unlock events | AG-05 | SK-05 | P1 | 4 | T-M2-000 | spikes/SPK-01.md | Captured on ≥ 5 devices; verdict | AG-03 | In Progress |
| T-M2-002 | Google Play policy review for all permissions | AG-05 | SK-05 | P1 | 2 | T-M2-000 | spikes/SPK-02-policy.md | Policy text quoted and dated | AG-09 | Done |
| T-M2-003 | Spike: notification counts & activity recognition | AG-05 | SK-05 | P2 | 3 | T-M2-001 | spikes/SPK-03.md | Verdict per signal | AG-03 | In Progress |
| T-M2-004 | Spike: background scheduling on top-5 OEMs | AG-05 | SK-05 | P1 | 4 | T-M2-001 | spikes/SPK-04.md | Gap rates per OEM | AG-03 | In Progress |
| T-M2-005 | Battery & inference budget benchmark | AG-05 + AG-06 | SK-05;SK-06 | P1 | 3 | T-M2-004 | spikes/SPK-05.md | Meets or revises NFR-02/04 | Tech Lead | In Progress |
| T-M2-006 | Baseline experiment on public dataset (cold-start rules vs LR/GBT) | AG-06 | SK-06 | P2 | 5 | T-M2-000;T-M1-013 | EXP-0001..; FS-001 v1 | Logged in registry; reviewed | AG-07 | Blocked |
| T-M2-007 | Threat model v1, data dictionary v1, retention & delete-all design | AG-09 | SK-09 | P1 | 4 | T-M2-001 | threat-model.md, data-dictionary.md | Every flow has threats | Tech Lead | Done |
| T-M2-008 | Catalogue pipeline & approved-only recommender constraints design | AG-03 + AG-08 | SK-03;SK-08 | P1 | 2 | T-M1-010 | ADR-0005, api-contracts §2 | Signing + filter specified | AG-09 | Done |
| T-M2-009 | Architecture views, module map, interfaces; accept ADRs | AG-03 | SK-03 | P1 | 5 | T-M2-001;T-M2-004;T-M2-007 | 03-architecture/* | Tech Lead approval | Tech Lead | Done |
| T-M2-010 | Evaluation plan EP-001 v1 | AG-07 | SK-07 | P1 | 2 | T-M1-013 | EP-001 | Ship criteria defined | Tech Lead | Done |
| T-M2-011 | Low-fi UX flows (onboarding, check-in, recommendation, safety, privacy) | AG-12 | SK-12 | P2 | 5 | T-M1-005 | 07-ux/flows/* | Trace to stories; AG-10 review | AG-10 | In Review |
| T-M2-012 | Test strategy and device matrix | AG-14 | SK-14 | P2 | 2 | T-M1-005 | 08-testing/test-strategy.md | Entry/exit criteria | AG-16 | Done |
| T-M2-013 | G2 gate pack and review | AG-01 | SK-01 | P1 | 1 | T-M2-002;T-M2-005;T-M2-009;T-M2-010 | gates/G2.md | Go/no-go per signal recorded | Lead + Tech Lead | In Progress |
| **M3** | | | | | | | | | | |
| T-M3-001 | Skill gap analysis for M4–M10 work | AG-00 | SK-00 | P1 | 2 | T-M2-013 | skills/gap-analysis.md | Every M4–M10 task type maps to a skill; no new agents | AG-16 | Done |
| T-M3-002 | Finalise agent definition files AG-00..AG-16 (aligned to AGS-04 O/C/G roles) | AG-00 | SK-00 | P1 | 2 | T-M3-001;T-M3-011 | agents/AG-*.md | Paths non-overlapping; reviewers set; Owner/Gate roles match AGS-04 | AG-16 | Done |
| T-M3-003 | Complete SK-11/SK-13/SK-15 with reference packs (Kotlin conventions, module templates, CI templates) | AG-03 + AG-15 | SK-00 | P1 | 4 | T-M3-001 | skills/SK-11,13,15/references | Templates + conventions present | Tech Lead | Done |
| T-M3-004 | Complete SK-06/SK-07 reference packs (feature→code templates, eval scripts) | AG-06 + AG-07 | SK-00 | P1 | 3 | T-M3-001 | skills/SK-06,07/references | Templates + checklists present | Tech Lead | Done |
| T-M3-005 | Content validator & catalogue CI (schema, refs, status rules) | AG-13 | SK-13;SK-08 | P1 | 3 | T-M2-008 | content/validation/*, CI job | Rejects invalid/unapproved items in tests | AG-16 | Done |
| T-M3-006 | Pilot dry-run: each implementation agent completes 1 real M4/M5 task | AG-00 (coordinating) | all | P1 | 5 | T-M3-002;T-M3-003;T-M3-004;T-M3-005 | pilot PRs, audit reports | All approved by AG-16; trap evals pass | AG-16 + Tech Lead | Done |
| T-M3-007 | CI checks: task-ID in PR, docs links, traceability IDs | AG-15 | SK-15 | P1 | 2 | T-M3-003 | .github/workflows/* | Blocking checks active | AG-16 | Done |
| T-M3-008 | Retrospective and skill v1.0 release | AG-00 | SK-00;SK-01 | P1 | 2 | T-M3-006 | CHANGELOG, skills v1.0 | All skills v1.0 | Project Lead | Done |
| T-M3-009 | Decompose M4–M5 backlog into Ready tasks with HC-01 packets (Mode/O/C/G fields) | AG-00 | SK-01 | P1 | 3 | T-M3-008;T-M3-011 | task-register.md (M4, M5) | ≥ 2 weeks of Ready work per active agent; each HC-01 has mode, owner, collaborators, gates | AG-16 | In Review |
| T-M3-010 | G3 gate pack and review | AG-01 | SK-01 | P1 | 1 | T-M3-007;T-M3-009 | gates/G3.md | Lead + Tech Lead approve | Lead + Tech Lead | Done |
| T-M3-011 | Orchestration redesign: AGS-02 v0.2 + AGS-04 collaboration matrix (D-007) | AG-00 | SK-00;SK-01 | P1 | 2 | T-M0-002 | orchestration-policy.md, agent-collaboration.md, HC-01, AGENTS.md §5 | Matrix is selection authority; AG-00 process-only; Mode/O/C/G on HC-01 | AG-16 | Done |
| T-M3-012 | Cursor/GitNexus/MCP efficiency tooling (lean rules, index, orchestrate wrapper) | AG-00 | SK-00 | P1 | 2 | T-M3-011 | .cursor/rules, tooling-efficiency.md, .gitnexus index | BCSR in GitNexus registry; AGENTS.md stays lean | AG-16 | Done |
| **M4** | | | | | | | | | | |
| T-M4-001 | Draft metadata for 60–100 seed items (finding aids → primary references; states per taxonomy v1) | AG-08 | SK-08 | P1 | 8 | T-M3-010;T-M1-010 | content/catalogue/items/*.json (draft); HC-06 packs | Schema valid; no agent-written scripture; every item has a primary reference to verify; HC-06 pack per batch | Scholar | Backlog |
| T-M4-002 | Import Tanzil Qur'an text verbatim + surah/ayah metadata; validator uses metadata | AG-13 | SK-13;SK-08 | P1 | 3 | T-M3-010;T-M1-010 | scripts/import_tanzil.py; content/sources/tanzil/ (with licence + attribution) | Checksum matches the Tanzil download; attribution stored (CR-07); Tanzil notice kept in the imported file and every bundle; About notice matches it; validate_catalogue.py ayah table replaced | AG-16 | Backlog |
| T-M4-003 | Choose Hadith Arabic edition, Urdu/English translations and transliteration with the scholar; record licences | AG-04 | SK-04;SK-08 | P1 | 3 | T-M3-010;T-M1-010 | RES-03 §3.2 rows; CON-01 §1 filled | Each source licensed and attributed; scholar's choice recorded | Scholar | Backlog |
| T-M4-004 | Text entry of Arabic, transliteration and translations from the chosen sources (two-person rule) | Human editor | — | P1 | 10 | T-M4-001;T-M4-002;T-M4-003 | content/catalogue/items/*.json (text fields) | Text copied verbatim from the recorded source; status in_review | Scholar | Backlog |
| T-M4-005 | Scholar review of every item and its state mapping; review records RR-NNNN | Scholar | — | P1 | 15 | T-M4-004 | docs/05-spiritual-content/review-records/RR-*.md | ≥ 60 items approved with records; CI validator passes | Project Lead | Backlog |
| T-M4-006 | Bundle signing tool and offline key procedure (ADR-0005 §4, §8) | AG-13 | SK-13 | P1 | 3 | T-M3-010 | scripts/sign_bundle.py; docs/09-release-operations/catalogue-signing.md | Ed25519 sign/verify round trip in tests; no key in the repo; rotation steps written | AG-09 | Done |
| T-M4-007 | CI: build the bundle on catalogue changes; check both hash test vectors | AG-15 | SK-15 | P2 | 2 | T-M4-006 | .github/workflows/governance.yml | CI fails if the bundle build or vectors fail; gates unchanged | AG-09 | Done |
| **M5** | | | | | | | | | | |
| T-M5-001 | Product app skeleton: :app, Compose, Hilt, navigation, modules per ARC-02 §2 | AG-11 | SK-11 | P1 | 4 | T-M3-010 | app/android/** (modules) | Builds in CI; module map matches ARC-02 §5; :core:policy kept | AG-03 | Done |
| T-M5-002 | :core:data — Room + SQLCipher schema (ARC-04), Keystore key, retention job | AG-11 | SK-11;SK-09 | P1 | 5 | T-M5-001 | app/android/core/data/** | AC-PR-02, AC-PR-06 tests pass | AG-09 | In Review |
| T-M5-003 | :core:sensing — usage, screen/unlock, notification counts, activity collectors | AG-11 | SK-11;SK-05 | P1 | 6 | T-M5-002;T-M2-013 | app/android/core/sensing/** | AC-FR-03..FR-06, AC-PR-01; only signals with a G2 Go are enabled | AG-09 | Backlog |
| T-M5-004 | :core:features — Kotlin FS-001 matching ml/features reference values | AG-11 | SK-11;SK-06 | P1 | 5 | T-M5-001 | app/android/core/features/** | Parity tests against the Python fixtures; gap_flag set on missed windows | AG-07 | Done |
| T-M5-005 | :core:inference — cold-start rules model (B1) and StateEstimator runtime interface | AG-06 | SK-06 | P1 | 4 | T-M5-004 | app/android/core/inference/**; ml/training/coldstart.md | AC-FR-10 (6 states), AC-FR-11 | AG-07 | Done |
| T-M5-006 | :core:content — bundle loader, Ed25519 verify, runtime approved-only filter | AG-11 | SK-11 | P1 | 4 | T-M5-001;T-M4-006 | app/android/core/content/** | TC-FR-15-04 property test; both hash test vectors reproduced in Kotlin; bad signature keeps last good bundle | AG-09 | Done |
| T-M5-007 | :core:recommender — constrained contextual bandit and 'why' strings | AG-06 | SK-06 | P1 | 5 | T-M5-006 | app/android/core/recommender/** | AC-FR-16, AC-FR-29, AC-NFR-08 | AG-07 | Done |
| T-M5-008 | Wire :core:policy to Adhan Salah times and WorkManager scheduling | AG-11 | SK-11 | P1 | 3 | T-M5-001 | app/android/core/policy/**, :app | TC-FR-21-04 instrumented; CR-REQ-0001 outcome applied | AG-14 | In Review |
| T-M5-009 | Onboarding & consent (UX-01) and Privacy Center (UX-05) | AG-11 | SK-11;SK-12 | P1 | 6 | T-M5-002 | app/android/feature/onboarding/**, feature/privacy/** | AC-FR-01, AC-FR-02, AC-FR-25, AC-FR-26, AC-PR-03, AC-PR-05 | AG-12 | In Review |
| T-M5-010 | Check-in and recommendation card (UX-02, UX-03) | AG-11 | SK-11;SK-12 | P1 | 5 | T-M5-007 | app/android/feature/checkin/**, feature/library/** | AC-FR-13, AC-FR-14, AC-FR-18, AC-FR-20 | AG-12 | In Review |
| T-M5-011 | Safety screen (UX-04 / SAF-01) and helpline directory data | AG-11 | SK-11;SK-10 | P1 | 3 | T-M5-001 | app/android/feature/safety/**, core/safety/** | AC-FR-24, AC-SR-01..SR-05; copy matches SAF-01 v1.0 | Project Lead | In Review |
| T-M5-012 | Research ingestion backend (opt-in): OpenAPI, server, pseudonymised store | AG-13 | SK-13;SK-09 | P2 | 6 | T-M3-010;T-M1-012 | app/backend/** | AC-PR-03; TH-04, TH-13 mitigations tested | AG-09 | Backlog |
| T-M5-013 | Automated gate test suites: privacy, safety, content (TST-01 §2) | AG-14 | SK-14 | P1 | 6 | T-M5-003 | tests/**, app/android/**/androidTest | Every AC-PR-*, AC-SR-*, AC-CR-* has an automated or inspection case | AG-16 | In Progress |
| T-M5-014 | Device-matrix runs of the product build (TST-02) | AG-14 | SK-14 | P1 | 5 | T-M5-003 | docs/08-testing/device-matrix.md; tests/evidence/ | ≥ 5 devices; gate tests pass on each | AG-16 | Backlog |
| T-M5-015 | CI: Android build, SBOM, dependency verification, actions pinned by SHA | AG-15 | SK-15 | P1 | 5 | T-M5-001 | .github/workflows/* | SBOM per build; Gradle dependency verification on; all actions SHA-pinned | AG-09 | Done |
| T-M5-016 | Play internal-testing track, app signing, release checklist draft | AG-15 | SK-15 | P2 | 4 | T-M5-015 | docs/09-release-operations/* | Internal track build uploaded; signing keys outside the repo | AG-09 | In Progress |
| T-M5-017 | Play Data safety form and prominent-disclosure check; Usage Access declaration | AG-09 | SK-09 | P1 | 3 | T-M5-009;T-M2-002 | docs/06-security-privacy-safety/play-data-safety.md | AC-PR-05 against current Play policy; R-02 updated | Project Lead | In Review |
| T-M5-018 | Data protection impact assessment (DPIA) for D1/D2 | AG-09 | SK-09 | P1 | 5 | T-M3-010 | docs/06-security-privacy-safety/dpia.md | Every ARC-05 flow assessed; residual risks owned | Project Lead | In Review |
| T-M5-019 | Security review of sensing, content loader and backend | AG-09 | SK-09 | P1 | 3 | T-M5-006;T-M5-012 | docs/06-security-privacy-safety/security-review-M5.md | Findings fixed or accepted by the Tech Lead | Tech Lead | Backlog |
| T-M5-020 | Hi-fi screens and en/ur microcopy for UX-01..05 (state words from interviews) | AG-12 | SK-12 | P1 | 8 | T-M3-010;T-M1-003 | docs/07-ux/hifi/* | Traces to stories; RTL and a11y checklist; AG-10 review of safety copy | AG-10 | Backlog |
| T-M5-021 | Model and data cards for the cold-start model, D1 and D2 | AG-07 | SK-07 | P2 | 2 | T-M5-005 | docs/04-ai-ml/model-cards/* | AC-PR-07; subgroup plan from EP-001 | Tech Lead | In Review |
| T-M5-022 | Internal dogfooding D1: team members, consented, 2 weeks; data-quality report | AG-07 | SK-07 | P1 | 10 | T-M5-013;T-M5-011;T-M5-018 | docs/04-ai-ml/D1-report.md | Consent recorded; gap rate, EMA burden and battery reported | Project Lead | Backlog |
| T-M5-023 | Pilot study D2: ≥ 30 participants, 4 weeks (RES-04) | AG-07 | SK-07 | P1 | 20 | T-M5-022;T-M1-012 | docs/04-ai-ml/D2-report.md | IRB approval before start; EP-001 data collected | Project Lead | Backlog |
| T-M5-024 | About and credits screen: version, not-medical line, Tanzil attribution and link, licences | AG-11 | SK-11;SK-12 | P2 | 1 | T-M5-001 | app/android/app (About screen) | AC-SR-02 (about screen), AC-CR-07; gate check passes | AG-16 | In Review |

## Effort summary (agent+human days, before parallelism)

- M0: 16 days across 9 tasks
- M1: 58 days across 14 tasks
- M2: 45 days across 14 tasks
- M3: 31 days across 12 tasks
- M4: 44 days across 7 tasks
- M5: 127 days across 23 tasks
