# Data Dictionary — ARC-04 v0.1

| Entity | Field | Type | Sensitivity | Notes |
|---|---|---|---|---|
| usage_event | id, ts_start, ts_end | long (epoch ms) | High | Raw; 30-day retention |
| | package_hash | string (salted SHA-256) | High | Raw package names are not stored after categorisation, except in the local category cache |
| | category | enum (SOCIAL, VIDEO, MESSAGING, GAMES, PRODUCTIVITY, BROWSER, RELIGIOUS, OTHER…) | Medium | |
| device_event | ts, type (SCREEN_ON, SCREEN_OFF, UNLOCK, CHARGING_ON/OFF) | | Medium | |
| activity_event | ts, activity (STILL, WALKING, VEHICLE, …) | | Medium | Optional |
| notif_count | window_start, category, count | | Medium | No content, no titles |
| feature_window | window_start, length, feature vector (JSON/blob), baseline z-scores | | Medium | Spec in feature-specifications |
| state_estimate | ts, model_version, probs[9] | | Medium | |
| label | ts, valence, energy, state_id, source (EMA/confirm) | | High | |
| recommendation | ts, item_id, state_id, policy_version, feedback, dwell_ms | | Low | |
| catalogue_item | see content-schema.md | | Public | Read-only, signed |
| settings | quiet hours, languages, Salah calc method, caps | | Low | |
