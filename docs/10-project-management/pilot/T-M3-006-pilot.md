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
requirements: [NFR-07]   acceptance_criteria: [jobs run and pass on this PR; no existing gate removed]
constraints: [do not disable blocking checks (SK-15)]   reviewer: AG-16
```

## HC-03 submissions
| Pilot | Outputs | Evidence | Known limitations |
|---|---|---|---|
| P1 AG-13 | `scripts/build_catalogue_bundle.py` | Real catalogue: 0 approved, DUA-0001 excluded; P3 tests 6/6 | No signing (by design, ADR-0005 §4); `/build/` ignored in Git |
| P2 AG-11 | `app/android/core/policy` (`decide`, `TimingSettings`, `Decision`) | 7/7 JVM tests incl. exhaustive minute-by-minute check | Salah times passed in; wiring to Adhan and WorkManager is M5 |
| P3 AG-14 | `TC-FR-15-FR-21.md`, `tests/test_build_catalogue_bundle.py` | 6/6; 2,000-catalogue property test | Found AC-FR-21 ambiguity (when to drop vs defer) → HC-07 to AG-02 |
| P4 AG-06 | `ml/features/fs001.py`, tests | 5/5 fixture tests | z-score uses unscaled MAD as FS-001 states; scaling question for AG-07 |
| P5 AG-15 | `governance.yml`: ML step in `checks`, new `kotlin-tests` job | CI run on this PR | Spike tests need the runner's Android SDK |

All five pilots were written in one session by the same model playing each role; that is why the gate is a **separate** AG-16 subagent.

## AG-16 audit
See `docs/10-project-management/audits/T-M3-006-audit.md` (written by the AG-16 subagent, not by the authors).
