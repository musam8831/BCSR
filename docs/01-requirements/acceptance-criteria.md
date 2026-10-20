# Acceptance Criteria — REQ-04 v0.1

Gherkin-style criteria for the Must requirements. AG-02 completes the remainder in T-M1-005.

**AC-FR-02 (check-in-only mode)**
Given the user declines Usage Access and activity permissions, when onboarding completes, then the home screen and check-ins work, recommendations are based on the check-in and time of day only, and no sensing service is scheduled.

**AC-FR-10 (state inference)**
Given ≥ 1 complete feature window, when inference runs, then a probability vector over CS-01..CS-09 summing to 1 (±1e-6) is produced in ≤ 100 ms on the reference device and is stored with the model version.

**AC-FR-14 (contextual check-in)**
Given the top state probability ≥ the configured threshold and no check-in in the last N minutes and not in quiet hours or the Salah window, when inference completes, then one check-in notification is posted whose copy is phrased as a question.

**AC-FR-15 (approved content only)**
Given a catalogue containing items with status draft, in_review, rejected and approved, when any recommendation is generated, then 100% of returned items have status approved (verified by property test over 10,000 random contexts).

**AC-FR-18 (item display)**
Given an approved item, when it is opened, then Arabic (RTL, proper font), transliteration, the selected-language translation and the reference string are shown, and the reference matches the catalogue exactly.

**AC-FR-21 (quiet hours & Salah)**
Given quiet hours 23:00–06:00 and computed Salah times, when a notification would fall inside them (± a configured buffer), then it is deferred or dropped and is never shown.

**AC-FR-24 / SR-01 (safety)**
Given the user selects a crisis option in a check-in or taps "I need help now", then within one screen the app shows verified helplines for the user's region and an emergency-call action, and logs nothing beyond a local counter.

**AC-FR-25 (delete all)**
Given stored data, when the user confirms delete-all, then the encrypted DB, preferences, model personalisation weights and cached features are erased, and the app returns to onboarding.

**AC-PR-01 (no content)**
Static analysis and manifest review confirm no AccessibilityService, no IME, no READ_SMS/READ_CALL_LOG, no screen-capture APIs, and that the notification listener (if used) stores only package/category and timestamp.
