# Test cases — privacy (AC-PR-*), safety (AC-SR-*) and content (AC-CR-*) gates

Task T-M5-013 · Owner: AG-14 (SK-14) · Strategy: TST-01 §2. These are **gate tests**: they are never skipped, and "no evidence" counts as a fail. `tests/test_gate_cases.py` fails if an AC-PR, AC-SR or AC-CR in REQ-03 has no row here.

**Type** says how the case is checked:
- **A**: automated in CI.
- **A-dev**: automated, but needs a device or emulator.
- **I**: inspection before release, by the checker named in the row; the release checklist links its record.

**Status:**
- **Pass**: CI is green on `main`.
- **Pending X**: the case is written but cannot run until X exists.

| TC | AC | Type | Case | Automated in / checked by | Status |
|---|---|---|---|---|---|
| TC-PR-01a | AC-PR-01 | A | The merged debug and release manifests have no BIND_ACCESSIBILITY_SERVICE, BIND_INPUT_METHOD, READ_SMS, READ_CALL_LOG or MEDIA_PROJECTION. Both `uses-permission` tags are scanned. | `scripts/check_android_gates.py` (CI `kotlin-tests`), `tests/test_android_gates.py` | Pass |
| TC-PR-01b | AC-PR-01 | A | The store schema has no package-name or message-text columns: hash and category only. | `:core:data` `RetentionTest.schemaHasEveryTableAndNoPackageNames` | Pass |
| TC-PR-01c | AC-PR-01 | I | The notification-listener code stores only package hash, category and timestamp. | AG-09, when T-M5-003 lands | Pending T-M5-003 |
| TC-PR-02a | AC-PR-02 | A-dev | Copy the database file. It does not open as plain SQLite, and its key is in the Android Keystore. | `:core:data` `EncryptionTest.databaseFileIsNotPlainSqlite` (androidTest) | Pending device run (T-M5-014) |
| TC-PR-02b | AC-PR-02 | I | The key comes from the Keystore (AES-GCM, not exportable), with no key in code or prefs. | AG-09 review of `BcsrStore.kt` | In review (T-M5-002) |
| TC-PR-03a | AC-PR-03 | A | Nothing can be uploaded: no INTERNET permission in any merged manifest. | `check_android_gates.py` | Pass |
| TC-PR-03b | AC-PR-03 | A | Opt-in shows each data type separately. Withdrawing stops uploads at once. | to be written with the upload module | Pending T-M5-012 (needs ethics T-M1-012) |
| TC-PR-04a | AC-PR-04 | A | No ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION or ACCESS_BACKGROUND_LOCATION in the merged manifests. | `check_android_gates.py` | Pass |
| TC-PR-04b | AC-PR-04 | A | No coordinate or location column in the store schema. | `RetentionTest.schemaHasEveryTableAndNoPackageNames` | Pass |
| TC-PR-05 | AC-PR-05 | I | Onboarding shows the prominent disclosure before the Usage Access request, with every element the Play policy requires (T-M2-002). | AG-09 + Project Lead with the Data safety form (T-M5-017) | Pending T-M5-003 (Usage Access not requested yet) |
| TC-PR-06a | AC-PR-06 | A | Cut-offs are 30 days for raw data and 90 days for derived data. Labels, favourites and settings are kept. | `RetentionTest.cutoffs`, `statementsCoverRawAndDerivedButNotLabels` | Pass |
| TC-PR-06b | AC-PR-06 | A-dev | The daily job deletes old rows, and the store stays ≤ 50 MB. | device matrix | Pending T-M5-014 |
| TC-PR-07 | AC-PR-07 | I | The model card and experiment registry list only D1–D3 consented data. The cold-start model is rules, with no training data. | Tech Lead, `docs/04-ai-ml/model-cards/MC-coldstart-0.1.md` | In review (T-M5-021) |
| TC-SR-01a | AC-SR-01 | A | A release shows only helplines verified within 90 days. Unverified ones are never shown, and stale entries block a release. | `:core:safety` `HelplinesTest` | Pass |
| TC-SR-01b | AC-SR-01 | A-dev | One tap from Home reaches the safety screen, and the call buttons dial. | device matrix (AC-FR-24) | Pending T-M5-014 |
| TC-SR-02 | AC-SR-02 | A | Onboarding, the safety screen and About each show `not_medical`. This is checked per screen, so commented-out code does not count. | `check_android_gates.py` | Pass |
| TC-SR-03a | AC-SR-03 | A | Inference outputs only passive states, so it never outputs a safety state. | `:core:inference` `ColdStartRulesTest.outputsOnlyPassiveStatesSummingToOne` (1,000 random windows) | Pass |
| TC-SR-03b | AC-SR-03 | A | The safety screen opens only from "I need help now" or the check-in "not safe" answer, never from other code or the worker. | `check_android_gates.py` | Pass |
| TC-SR-04 | AC-SR-04 | A | The shipped en/ur safety strings equal the signed SAF-01 §4 table. Changing the table without re-signing fails. | `check_android_gates.py` | Pass |
| TC-SR-05 | AC-SR-05 | A + I | Every directory entry has a review date within 3 months (code). The quarterly review is recorded in SAF-02. | `HelplinesTest.currentDirectoryBlocksRelease`; Project Lead | Pass (code); review due per SAF-02 |
| TC-CR-01 | AC-CR-01 | A | Every item has a primary reference. Qur'an ranges are checked against the ayah table. Hadith has collection, number, numbering system, grading and grader. | `tests/test_validate_catalogue.py` | Pass |
| TC-CR-02 | AC-CR-02 | A | An approved item without a named-reviewer record fails CI. The bundle holds only approved items. | `test_validate_catalogue.py`, `test_build_catalogue_bundle.py`, `RecommenderTest.onlyApprovedMappedItemsAreReturned` | Pass |
| TC-CR-03 | AC-CR-03 | I | The Arabic text matches the recorded edition byte for byte. The licence is listed in RES-03. | AG-13 + scholar | Pending M4 (T-M4-002/004) |
| TC-CR-04 | AC-CR-04 | I | Each translation names a licensed, attributed source or an approved translator. | AG-13 + scholar | Pending M4 (T-M4-003) |
| TC-CR-05 | AC-CR-05 | I | The review record shows the scholar approved each state mapping. | scholar | Pending M4 (T-M4-005) |
| TC-CR-06 | AC-CR-06 | A + I | Da'if grading fails the schema (code). The scholar confirms the D-010 manhaj per item. | `test_validate_catalogue.test_daif_grading_fails_schema`; scholar | Pass (code); pending M4 (scholar) |
| TC-CR-07 | AC-CR-07 | A | About shows the Tanzil credit, its copyright notice, CC BY 3.0 and a tanzil.net link, and Home links to About. | `check_android_gates.py` | Pass. Re-check against the imported file header in T-M4-002 |

## Still open for T-M5-013
- **Pending device runs:** the androidTest and A-dev cases run on the device matrix (T-M5-014).
- **Waiting on other tasks:** TC-PR-01c and TC-PR-05 need T-M5-003, and TC-PR-03b needs T-M5-012.
- **Waiting on the scholar:** the content inspections TC-CR-03 to TC-CR-06 need the M4 scholar review.
