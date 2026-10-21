# T-M3-006 Pilot dry-run — packets and submissions

Coordinator: AG-00 · Gate: **AG-16 (independent subagent)** + Tech Lead · Acceptance: all pilot tasks approved by AG-16; trap evals pass.
Each implementation agent (AG-06, AG-11, AG-13, AG-14, AG-15) completes one real M4/M5 task. The outputs are kept and become the starting point of those M4/M5 tasks (T-M3-009).

## HC-01 packets
```
task_id: T-M3-006 / P1          title: Catalogue bundle builder (ADR-0005 §3)
mode: standard-workstream       owner: AG-03 (ADR-0005)   collaborators: [AG-08]   gates: [AG-16, AG-09]   informed: [AG-14]
agent: AG-13   skills: [SK-13, SK-08]
objective: build the app bundle from approved items only, with a manifest and SHA-256; no keys
inputs: [ADR-0005, api-contracts §2, content/validation/validate_catalogue.py]
outputs: [scripts/build_catalogue_bundle.py]
requirements: [FR-15, CR-02]   acceptance_criteria: [AC-FR-15 (build side); invalid catalogue builds nothing]
constraints: [AGENTS.md §2: never edit scripture fields; signing stays offline (ADR-0005 §4)]
reviewer: AG-16   considered_not_needed: [Scholar gate: no content changed]
```
```
task_id: T-M3-006 / P2          title: :core:policy notification timing (FR-21)
mode: standard-workstream       owner: AG-03   collaborators: [AG-12]   gates: [AG-16, AG-14]   informed: [AG-10]
agent: AG-11   skills: [SK-11]
objective: pure-Kotlin rule: allow, defer to the end of quiet hours / Salah window, or drop at the daily cap
inputs: [FR-21, AC-FR-21, ARC-02 §2 (module :core:policy), ARC-03 step 4]
outputs: [app/android/settings.gradle.kts, app/android/core/policy/**]
requirements: [FR-21]   acceptance_criteria: [AC-FR-21]
constraints: [no AccessibilityService/IME; no paid LLM]   reviewer: AG-16
considered_not_needed: [driving-state rule: needs activity input, later M5 task]
```
```
task_id: T-M3-006 / P3          title: Test cases for FR-15 and FR-21; independent tests of P1
mode: standard-workstream       owner: AG-14   collaborators: [AG-13, AG-11]   gates: [AG-16]   informed: [AG-02]
agent: AG-14   skills: [SK-14]
objective: test cases traced to ACs; property tests of the bundle builder written by someone other than its author
inputs: [TST-01, AC-FR-15, AC-FR-21, P1, P2]
outputs: [docs/08-testing/cases/TC-FR-15-FR-21.md, tests/test_build_catalogue_bundle.py]
requirements: [FR-15, FR-21]   acceptance_criteria: [cases map to ACs; gate tests automated where possible]
reviewer: AG-16
```
```
task_id: T-M3-006 / P4          title: First FS-001 features and personal baseline z-score
mode: standard-workstream       owner: AG-06   collaborators: [AG-07]   gates: [AG-16, AG-07]   informed: [AG-11]
agent: AG-06   skills: [SK-06]
objective: reference implementation of unlock_count, checking_sessions, app_switch_entropy, robust_z
inputs: [FS-001, ARC-04, SK-06 feature-to-code template]
outputs: [ml/features/fs001.py, ml/features/test_fs001.py]
requirements: [FR-08, FR-09]   acceptance_criteria: [values match FS-001 definitions on fixtures]
constraints: [on-device signals only; no raw export; no paid LLM]   reviewer: AG-16
```
```
task_id: T-M3-006 / P5          title: CI job for Kotlin and ML tests
mode: governance-epic           owner: AG-15   collaborators: []   gates: [AG-16, AG-09]   informed: [AG-11, AG-06]
agent: AG-15   skills: [SK-15]
objective: CI runs :core:policy tests, spike unit tests and ML feature tests on every PR
inputs: [TST-01 §1, SK-15 CI templates, .github/workflows/governance.yml]
outputs: [.github/workflows/governance.yml]
requirements: [— (test infrastructure, TST-01 §1); NFR-07 coverage measurement deferred to M5]   acceptance_criteria: [jobs run and pass on this PR; wrapper jars validated; no existing gate removed]
constraints: [do not disable blocking checks (SK-15)]   reviewer: AG-16
```

## HC-03 submissions
| Pilot | Outputs | Evidence | Known limitations |
|---|---|---|---|
| P1 AG-13 | `scripts/build_catalogue_bundle.py` | Real catalogue: 0 approved, DUA-0001 excluded; P3 tests 6/6 | No signing (by design, ADR-0005 §4); `/build/` ignored in Git |
| P2 AG-11 | `app/android/core/policy` (`decide`, `TimingSettings`, `Decision`) | 9/9 JVM tests incl. minute-by-minute check and 3,000 random settings | Salah times passed in; wiring to Adhan and WorkManager is M5 |
| P3 AG-14 | `TC-FR-15-FR-21.md`, `tests/test_build_catalogue_bundle.py` | 7/7; 2,000-catalogue property test; two hash test vectors | Found AC-FR-21 ambiguity (when to drop vs defer) → HC-07 to AG-02 |
| P4 AG-06 | `ml/features/fs001.py`, tests | 5/5 fixture tests | z-score uses unscaled MAD as FS-001 states; scaling question for AG-07 |
| P5 AG-15 | `governance.yml`: ML step in `checks`, new `kotlin-tests` job with Gradle wrapper validation | CI run on this PR | Spike tests need the runner's Android SDK; coverage (NFR-07) not measured yet |

All five pilots were written in one session by the same model playing each role; that is why the gate is a **separate** AG-16 subagent.

## Rework after the AG-16 audit (round 1: changes-requested)
| Finding | Fix |
|---|---|
| P1 major: `items_sha256` input undefined | Canonical JSON specified in api-contracts §2 with a cross-language test vector; `test_digest_matches_api_contract_test_vector` |
| P3 major: HC-07 claimed but not filed | Filed CR-REQ-0001 (drop vs defer) and CR-REQ-0002 (AC-FR-09 vs FS-001, checking-session definition) in `docs/10-project-management/change-requests/` |
| P2 minor: chaining bound wrong for extreme settings | Loop until free, drop after 48 h (as CR-REQ-0001 proposes); new 3,000-case random test checks every deferral target is free; always-blocked case drops |
| P3 minor: real-catalogue test trivial | Renamed to a smoke test, with the reason |
| Traceability Test column empty | FR-08, FR-09, FR-15, FR-21 rows link the tests |
| P5 minor: wrapper jar unvalidated; NFR-07 claimed | `gradle/actions/wrapper-validation@v4` step; NFR-07 claim removed, coverage deferred to M5 |
| SK-13, SK-15 edge evals fail | Rules added (SK-13 step 4, SK-15 step 6); both skills v0.3 with results |

## Gate record
| Pilot | Gates in packet | Recorded |
|---|---|---|
| P1 | AG-16, AG-09 | AG-16: approve-with-minors (round 2). AG-09 scope → Tech Lead (D-013): **accepted** 2026-10-21 |
| P2 | AG-16, AG-14 | AG-16: approve-with-minors (round 2). AG-14: TC-FR-21 cases written against P2 (P3) |
| P3 | AG-16 | AG-16: approve-with-minors (round 2) |
| P4 | AG-16, AG-07 | AG-16: approve-with-minors (round 2). AG-07 scope → Tech Lead (D-013): **accepted** 2026-10-21; plain MAD kept |
| P5 | AG-16, AG-09 | AG-16: approve-with-minors (round 2). AG-09 scope → Tech Lead (D-013): **accepted** 2026-10-21; SHA pinning carried forward |

### Tech Lead co-gate checklist (D-013)
- **AG-09 scope, P1 bundle builder:** reads only catalogue files, no behavioural data; never handles signing keys (ADR-0005 §4); writes only to the ignored `/build/` folder.
- **AG-09 scope, P5 CI:** jobs have read-only `contents` permission; no secrets used; Gradle wrapper jars validated; third-party actions pinned by major tag, not SHA (AG-16 nit, carried to T-M3-009).
- **AG-07 scope, P4 features:** z-score uses unscaled MAD as FS-001 states; decide whether to scale by 1.4826 (CR-REQ-0002, AG-07 question); checking-session definition proposed in CR-REQ-0002.
Recorded decision: **accepted** — Muhammad Usman (Tech Lead, D-009), 2026-10-21. AG-09 items for P1 and P5 accepted; SHA pinning carried to T-M3-009. AG-07: z-score keeps plain (unscaled) MAD as FS-001 states.

## Open minors carried forward (to T-M3-009 / M5)
- P2: the 48 h drop rule implements CR-REQ-0001's *proposed* wording; change it if the CR is decided differently.
- P5: pin GitHub Actions by commit SHA.
- SK-13 and SK-15 eval #1 results were filled by the authors and not audited by AG-16.

Path-ownership deviations accepted by AG-00: the root `.gitignore` (`/build/`, P1) and the executable bit on both `gradlew` files (P5) sit outside the agents' write paths; both are repository configuration with no owner in the registry, recorded here rather than reassigned.

## AG-16 audit
See `docs/10-project-management/audits/T-M3-006-audit.md` (written by the AG-16 subagent, not by the authors).
