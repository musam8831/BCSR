# Test Strategy — TST-01 v1.0-rc

Task: T-M2-012 · Owner: AG-14 (SK-14) · Reviewer: AG-16 · Inputs: REQ-04 acceptance criteria, REQ-05 traceability, ARC-05 threat model.

## 1. Levels
| Level | What | Tools | Where |
|---|---|---|---|
| Unit | Domain logic: features (FS-001), baselines, policy rules, bandit, retention, gap analysis | JUnit (Kotlin), pytest (ML) | Every PR in CI |
| Property | Approved-only filter (AC-FR-15), probability vectors (AC-FR-10), quiet hours (AC-FR-21) | Kotlin property tests | Every PR |
| Contract | Catalogue bundle signature and manifest (ADR-0005), research batch schema | JUnit, JSON schema | Every PR |
| Content gate | Catalogue schema, review records, references (AC-CR-01..02) | `scripts/check_repo.py` (T-M3-005 extends it) | Every PR |
| Instrumented | Sensing collectors, WorkManager, permissions, delete-all, pause | AndroidX Test on emulator + real devices | Nightly / before release |
| UI and accessibility | Flows UX-01..05, TalkBack, 200% font, RTL (AC-NFR-05, AC-FR-28) | Compose UI tests, Accessibility Scanner | Before release |
| Device | OEM background behaviour, battery, latency (SPK-04, SPK-05, AC-NFR-02, AC-NFR-04) | Spike app, then the product on the device matrix | Per milestone, before G-Release |
| Privacy and security | Manifest and static checks (AC-PR-01, PR-04), DB encryption (AC-PR-02), threat-model mitigations | Lint rules, manifest diff, manual audit | Before each release; privacy audit in M9 |
| Safety | Safety pathway reachability and copy match (AC-FR-24, AC-SR-01..05) | UI tests + inspection against SAF-01 | Every release |

## 2. Gate tests are never skipped
Content (AC-CR-*), safety (AC-SR-*, AC-FR-24) and privacy (AC-PR-*) tests block a release if they fail or have not run. "No evidence" counts as a failure (SK-14).

## 3. Entry and exit criteria
| Phase | Entry | Exit |
|---|---|---|
| Feature development (M5–M8) | HC-02 spec and its ACs exist; device matrix row chosen | Unit and property tests pass; AC evidence linked in the PR |
| System test (M9) | Feature-complete build; catalogue bundle signed; SAF-02 helplines verified ≤ 3 months | Every Must AC has passing evidence in REQ-05's Test column; no open blocker defect; gate tests pass on ≥ 5 matrix devices |
| Pilot release | System test exit; ethics approval (T-M1-012); privacy audit passed | Pilot exit report (EP-001 S1–S7 evaluated) |
| G-Release | Pilot exit; safety reviewer (Project Lead, D-011) sign-off | Release checklist (M10) complete |

## 4. Evidence
Each passing AC links its evidence (test report, log or screenshot) in the REQ-05 Test column. Device logs go to `tests/evidence/<device-id>/`; personal data in logs is removed first.

## 5. Defects
Severity: **blocker** (gate test failure, data leak, crash in safety flow), **major**, **minor**. Blockers stop the release.
