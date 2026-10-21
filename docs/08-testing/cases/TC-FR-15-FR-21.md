# Test cases — FR-15 (approved content only) and FR-21 (quiet hours, Salah, cap)

Pilot P3 · Owner: AG-14 (SK-14) · Strategy: TST-01 · These are **gate tests** for content (FR-15): never skipped.

| TC | AC | Level | Steps | Expected | Automated in | Status |
|---|---|---|---|---|---|---|
| TC-FR-15-01 | AC-FR-15 | Property | Build bundles from 2,000 random catalogues mixing draft, in_review, approved, rejected | Bundle holds exactly the approved items; manifest count matches; every input item is either included or reported excluded | `tests/test_build_catalogue_bundle.py` | Pass (2026-10-21) |
| TC-FR-15-02 | AC-FR-15, ADR-0005 | Unit | Same approved items in every input order; then edit one item | One digest for all orders; a different digest after the edit | same | Pass |
| TC-FR-15-03 | AC-FR-15 | Unit | Make the validator report an error, run the builder | Exit 1, no bundle file written | same | Pass |
| TC-FR-15-04 | AC-FR-15 | Property (app) | 10,000 random contexts through `CatalogueRepository` / Recommender | 100% of returned items approved | M5 (`:core:content`) | Not yet built |
| TC-FR-21-01 | AC-FR-21 | Property | Every minute of a day with quiet hours 23:00–06:00, 5 Salah times, buffer 15 min | No "Allow" inside a blocked window; every deferral is later than now | `app/android/core/policy` `exhaustiveDayNeverAllowsBlockedMinutes` | Pass (AG-11 test, reviewed by AG-14) |
| TC-FR-21-02 | AC-FR-21 | Unit | Daily cap 4, already sent 4 | Drop | `capReachedDrops` | Pass |
| TC-FR-21-03 | AC-FR-21 | Unit | Quiet hours ending inside a Salah window | One deferral to the end of the Salah window | `overlappingWindowsAreChained` | Pass |
| TC-FR-21-04 | AC-FR-21 | Instrumented | Real notifications with computed Salah times on the device matrix | None shown inside windows | M5 (needs `:core:policy` wired to Adhan and WorkManager) | Not yet built |

Gap found while writing these cases: AC-FR-21 says "deferred or dropped" but not **when** to drop instead of defer. The policy drops only when the daily cap is reached and defers otherwise. Filed as [CR-REQ-0001](../../10-project-management/change-requests/CR-REQ-0001.md) (HC-07) for AG-02 before G1.
