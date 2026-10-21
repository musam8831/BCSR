# Acceptance Criteria — REQ-04 v1.0-rc

Criteria for **all 46 Must requirements** in SRS §5 (T-M1-005). Gherkin style (Given / When / Then). Method in brackets: **T** test, **I** inspection, **A** analysis/benchmark, **D** demonstration. "Reference device" is the mid-range device fixed in T-M2-005; "configured" values are set in EP-001 or the settings defaults.

## Functional
**AC-FR-01 (explain before asking)** [D, I]
Given a fresh install, when onboarding reaches any permission, then a screen naming the signal, what is and is not collected, and why is shown before the system permission dialog, and declining leaves the app usable.

**AC-FR-02 (check-in-only mode)** [T]
Given the user declines Usage Access and activity permissions, when onboarding completes, then the home screen and check-ins work, recommendations are based on the check-in and time of day only, and no sensing service is scheduled.

**AC-FR-03 (app-usage events)** [T]
Given Usage Access is granted, when the user opens and closes an app, then one event with package, category, start and end time (± 1 s) is stored, and nothing else about the app's content.

**AC-FR-04 (screen and unlock)** [T]
Given sensing is on, when the screen turns on, off, or the device is unlocked, then one event with type and timestamp is stored per occurrence.

**AC-FR-07 (category table)** [T]
Given the on-device category table, when an event for a known package is stored, then it carries that package's category; an unknown package is stored as "other" without any network lookup.

**AC-FR-08 (windowed features)** [T]
Given a recorded event sequence with known expected values, when feature extraction runs, then every feature defined in FS-001 for each window (15 min, 1 h, daily) matches the expected value within its stated tolerance.

**AC-FR-09 (personal baseline)** [T]
Given ≥ 7 days of features, when the baseline is computed, then a rolling mean and spread exist for each feature; with < 7 days, the baseline is marked "not ready" and the cold-start model is used.

**AC-FR-10 (state inference)** [T, A]
Given ≥ 1 complete feature window, when inference runs, then a probability vector over CS-01..CS-04, CS-06 and CS-08 summing to 1 (± 1e-6) is produced in ≤ 100 ms on the reference device and stored with the model version; no probability is produced for CS-05, CS-07 or CS-09.

**AC-FR-11 (cold start)** [T]
Given a new user without a ready baseline or personal model, when inference runs, then the rule-based model produces the output and its version is recorded.

**AC-FR-13 (check-in in ≤ 3 taps)** [D, T]
Given a check-in is opened, when the user records mood, energy and an optional context tag, then saving takes at most 3 taps, and the record stores valence, energy, tags and time.

**AC-FR-14 (contextual check-in)** [T]
Given the top state probability ≥ the configured threshold, no check-in in the last N configured minutes, and not in quiet hours or the Salah window, when inference completes, then one check-in notification is posted whose copy is phrased as a question.

**AC-FR-15 (approved content only)** [T]
Given a catalogue containing items with status draft, in_review, rejected and approved, when any recommendation is generated, then 100% of returned items have status approved (property test over 10,000 random contexts).

**AC-FR-16 (contextual ranking)** [T]
Given two contexts that differ only in state (or time, recent history, or feedback), when items are ranked, then the ranking changes as the bandit policy specifies, and an item rated "not relevant" in that context ranks lower afterwards.

**AC-FR-18 (item display)** [T, I]
Given an approved item, when it is opened, then Arabic (RTL, proper font), transliteration, the selected-language translation and the reference string are shown, and the reference matches the catalogue exactly.

**AC-FR-20 (browse, favourites, search)** [D, T]
Given the catalogue, when the user browses by situation, searches by an English or Urdu word in the title, or adds a favourite, then matching approved items are listed and favourites persist across restarts.

**AC-FR-21 (quiet hours and Salah)** [T]
Given quiet hours 23:00–06:00 and computed Salah times, when a notification would fall inside them (± a configured buffer), then it is deferred or dropped and is never shown; the daily notification cap is never exceeded.

**AC-FR-24 (safety pathway)** [T, D]
Given the user selects a crisis option in a check-in or taps "I need help now", then within one screen the app shows verified helplines for the user's region and an emergency-call action, and logs nothing beyond a local counter.

**AC-FR-25 (delete all)** [T]
Given stored data, when the user confirms delete-all, then the encrypted DB, preferences, model personalisation weights and cached features are erased, and the app returns to onboarding.

**AC-FR-26 (pause sensing)** [T]
Given sensing is on, when the user pauses it, then no new sensing events are stored until the user resumes, and the pause state survives a restart.

**AC-FR-28 (languages and RTL)** [D, I]
Given the UI language set to Urdu, English or Arabic, when any screen is shown, then all UI strings are in that language and Urdu and Arabic screens are laid out right-to-left without clipped text.

**AC-FR-29 (feedback)** [T]
Given a shown recommendation, when the user taps "helpful", "not now" or "not relevant", then the choice is stored with the item, context and time, and passed to the bandit as reward.

## Non-functional
**AC-NFR-01 (offline)** [T]
Given airplane mode, when onboarding, check-ins, recommendations, catalogue, safety screen and privacy center are used, then each works with no network request.

**AC-NFR-02 (inference latency)** [A]
Given the reference device, when inference runs 1,000 times on recorded windows, then the 95th-percentile time is ≤ 100 ms.

**AC-NFR-04 (battery)** [A]
Given the reference device with sensing on for 3 days of typical use, when battery stats are measured, then the app's share is ≤ 2% per day.

**AC-NFR-05 (accessibility)** [I, T]
Given every screen, when audited, then text contrast meets WCAG 2.2 AA, layouts work at 200% font scale, touch targets are ≥ 48 dp, and every control has a TalkBack label.

**AC-NFR-08 (why this du'a)** [T, D]
Given any recommendation, when the user opens "why am I seeing this", then a plain-language reason derived from the inputs (state, time, history) is shown, with no diagnostic wording.

**AC-NFR-09 (licences)** [I]
Given a release build, when its dependencies, models, datasets and content sources are listed, then each appears in RES-03 §3 with a compatible licence; the build fails review otherwise.

## Privacy
**AC-PR-01 (no content)** [I]
Static analysis and manifest review confirm no AccessibilityService, no IME, no READ_SMS/READ_CALL_LOG, no screen-capture APIs, and that the notification listener (if used) stores only package/category and timestamp.

**AC-PR-02 (encryption at rest)** [I, T]
Given a device with stored data, when the app's database file is copied off the device, then it cannot be opened without the key, and the key is held in the Android Keystore.

**AC-PR-03 (research consent)** [T, D]
Given a user who has not opted in to research, when the app runs, then nothing is uploaded; opting in shows each data type separately, and withdrawing stops all uploads at once.

**AC-PR-04 (no precise location)** [I, T]
Given any build, when the manifest and stored data are inspected, then no precise location permission is requested and no coordinates are stored; at most a derived home/away flag exists.

**AC-PR-05 (prominent disclosure)** [I]
Given onboarding, when it is reviewed against Google Play's prominent-disclosure policy (T-M2-002), then the disclosure screen appears before the Usage Access request and contains every element the policy requires.

**AC-PR-06 (retention)** [T]
Given raw events older than 30 days and derived data older than 90 days (ARC-04 §1), when the daily maintenance job runs, then they are deleted, and the store stays ≤ 50 MB (NFR-03).

**AC-PR-07 (training data source)** [I]
Given any model shipped in a release, when its model card and experiment-registry entry are inspected, then the training data lists only D1–D3 datasets collected with BCSR consent.

## Safety
**AC-SR-01 (helplines and emergency)** — covered by AC-FR-24, plus [I]: every helpline shown has a verification date within the last 90 days in the helpline directory.

**AC-SR-02 (not a medical service)** [I, D]
Given onboarding, the safety screen and the about screen, when they are reviewed, then each states that BCSR is not a medical or therapeutic service.

**AC-SR-03 (explicit input only)** [T]
Given any combination of passive features, when inference runs, then the safety pathway is never triggered; it is triggered only by an explicit check-in answer or the "I need help now" control.

**AC-SR-04 (approved safety copy)** [I]
Given the safety pathway copy and flow in a release, when compared with the safety pathway document, then they match the version signed by the Project Lead (D-011).

**AC-SR-05 (quarterly helpline review)** [I]
Given the helpline directory, when audited, then every entry has a review date no older than 3 months.

## Content
**AC-CR-01 (reference)** [T]
Given the catalogue, when validated, then every item has a primary reference: Surah and Ayah for Qur'an; collection, number, numbering system, grading and grader for Hadith (CI schema check).

**AC-CR-02 (approval record)** [T]
Given an item with status approved, when the catalogue is validated, then a review record with a named reviewer exists; otherwise CI fails.

**AC-CR-03 (Arabic source and licence)** [I]
Given any item, when audited, then its Arabic text matches the recorded source edition exactly, and the source's licence is in RES-03.

**AC-CR-04 (translation source)** [I]
Given any item, when audited, then its translation names a licensed, attributed source or an approved human translator.

**AC-CR-05 (mapping approved)** [I]
Given any item, when its states are audited, then the review record shows the scholar approved that state mapping.

**AC-CR-06 (manhaj)** [I]
Given any approved item, when audited, then its review record confirms it meets the D-010 manhaj (Qur'an or Sahih Hadith per the Ahl-e-Hadith tradition).

**AC-CR-07 (attributions)** [I, D]
Given a release build, when the about/credits screen is reviewed, then every attribution required by RES-03 §3.2 is shown (e.g. Tanzil Project with a link to tanzil.net).
