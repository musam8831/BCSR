# Data Dictionary — ARC-04 v1.0-rc

Task: T-M2-007 · Owner: AG-09 · All entities are stored only on the device in the SQLCipher database (PR-02) unless noted.

## 1. Entities
| Entity | Field | Type | Sensitivity | Retention (default) | Notes |
|---|---|---|---|---|---|
| usage_event | id, ts_start, ts_end | long (epoch ms) | High | **30 days** | Raw (FR-03) |
| | package_hash | string (salted SHA-256) | High | 30 days | Raw package names are not stored after categorisation, except in the local category cache (TH-09) |
| | category | enum (SOCIAL, VIDEO, MESSAGING, GAMES, PRODUCTIVITY, BROWSER, RELIGIOUS, OTHER…) | Medium | 30 days | FR-07 |
| device_event | ts, type (SCREEN_ON, SCREEN_OFF, UNLOCK, CHARGING_ON/OFF) | long, enum | Medium | 30 days | FR-04 |
| activity_event | ts, activity (STILL, WALKING, VEHICLE, …), transition | long, enum | Medium | 30 days | Optional (FR-06) |
| notif_count | window_start, category, count | long, enum, int | Medium | 30 days | No content, no titles (FR-05, PR-01, TH-08) |
| feature_window | window_start, length, feature vector, baseline z-scores, gap_flag | long, int, blob | Medium | **90 days** | FS-001; gap_flag marks windows with missed collection (TH-16) |
| state_estimate | ts, model_version, probs over CS-01..CS-04, CS-06, CS-08 | long, string, float[6] | Medium | 90 days | FR-10 (taxonomy v1) |
| label | ts, valence, energy, state_id, source (EMA / confirm) | long, int, int, enum, enum | High | Until the user deletes | Includes EMA-only states CS-05, CS-07, CS-09 |
| safety_counter | count | int | Medium | Until delete-all | Only a counter (AC-FR-24) |
| recommendation | ts, item_id, state_id, policy_version, feedback, dwell_ms | | Low | 90 days | FR-29 |
| bandit_state | per-arm parameters | blob | Low | Until delete-all | FR-16 |
| personal_model | weights / calibration | blob | Medium | Until delete-all | FR-12 |
| catalogue_item | see content-schema.md | | Public | Replaced by each signed bundle | ADR-0005 |
| settings | quiet hours, languages, Salah method, caps, pause | | Low | Until delete-all | |
| consent | version, research opt-ins per data type, timestamps | | Medium | Until delete-all; a record of withdrawal is kept for the research store | PR-03 |

Retention values are reconciled with ARC-03: raw events 30 days, derived data 90 days, labels until deleted. NFR-03's "default 90 days" applies to the derived store; the 50 MB cap applies to all of it. AC-PR-06 checks both limits.

## 2. Retention job
A daily WorkManager job (while charging or idle): delete rows older than their retention; then check total DB size; if > 50 MB, delete oldest raw events first (NFR-03). Logged only as a local count.

## 3. Delete-all design (FR-25)
1. User confirms twice ("Delete everything? This cannot be undone").
2. Cancel all WorkManager jobs and unregister activity updates and the notification listener's processing.
3. Close the database; delete the database file and its WAL/SHM files; delete the Keystore key (so any leftover copy is unreadable).
4. Clear preferences, cached feature files, the personal model, bandit state and the category cache.
5. If research consent was given: send one "withdraw" message so the research store deletes that participant's data (PR-03), then delete the consent record.
6. Return to onboarding. AC-FR-25 verifies that each store is empty afterwards.
