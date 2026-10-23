# Security review M5 — content loader and on-device store (part 1)

Task T-M5-019 · Reviewer: AG-09 (SK-09) · Gate: Tech Lead · Date: 2026-10-23 · **Status: part 1 of 3.**

- **Part 1 (this document):** the content loader and the on-device store, which exist now.
- **Part 2:** sensing, after T-M5-003.
- **Part 3:** the research backend, after T-M5-012.

The task closes when all three parts are reviewed and every finding is fixed or accepted by the Tech Lead.

**Scope (main branch after PR #29):**
- `:core:content` `Catalogue.kt`
- `:core:data` `BcsrStore.kt` and `Retention.kt`
- `app` `BcsrApp.kt`, `CheckIns.kt`, and the Privacy Center export in `MainActivity.kt`

**Method.** I read the code against the threats in the threat model (`docs/03-architecture/threat-model.md`): TH-01, TH-02, TH-09, TH-10, TH-14 and TH-17. I also checked the gate checks from T-M5-013 (`docs/08-testing/cases/TC-PR-SR-CR.md`).

## Findings

**Severity:**
- **High:** breaks an acceptance criterion or exposes personal data.
- **Medium:** could lose or expose data under a plausible fault.
- **Low:** hardening, or a risk that has no path today.

| ID | Severity | Area | Finding | Resolution |
|---|---|---|---|---|
| S-01 | High | Store (AC-PR-06) | `BcsrStore.runRetention` was never called. Raw events past 30 days and derived data past 90 days would never be deleted, and the store would never be held to 50 MB. | **Fixed.** `CheckInWorker` runs it once a day, using `Retention.due`, which is unit-tested and handles the clock moving backwards. The last-run time is kept in `setting.retention_at`. The device check stays in TC-PR-06b (T-M5-014). |
| S-02 | Medium | Store (TH-01, availability) | The wrapped database passphrase was written in place. A crash mid-write would leave a truncated key file, so the database could not be decrypted on every later start. | **Fixed.** The file is written to a temporary file and then renamed. |
| S-03 | Medium | Export (TH-10) | The plaintext export in `cache/export/` stayed until delete-all, so a copy of a user's whole history sat outside the encrypted store indefinitely. | **Fixed.** `AppContainer` deletes `cache/export/` at every start. The file lasts only for the session that shared it. Delete-all still clears the cache. |
| S-04 | Low | Export (integrity) | Export fields were not escaped. A comma or line break in a value would shift the columns, so the user's copy of their data would be misread. | **Fixed.** RFC 4180 quoting (`csv()`), unit-tested. |
| S-05 | Low | Store (injection) | `record()` checked the table against an allowlist but put the column names into SQL unchecked. All callers today pass constants. | **Fixed.** Column names must match `[a-z_]+`, and the column and value counts must agree. |
| S-06 | Medium | Store (availability) | If the Keystore key is lost or invalidated (OEM bugs, restore to another device), `passphrase()` throws on every start, and the app crashes on launch with no way out except clearing app data. | **Open: Tech Lead to accept or schedule.** Recommendation: catch the decrypt failure, then show the user a "your data could not be unlocked; start fresh" screen that calls delete-all. The data is unreadable anyway, so nothing more is lost. This needs a UX copy decision (AG-12), so it is not changed here. |
| S-07 | Low | Content (TH-02) | Bundle rollback: `CatalogueRepository.offer` accepts any validly signed bundle, including an older one that still holds an item the scholar later withdrew. Today the only bundle is the one shipped in the APK, so there is no path. | **Open: accept for now.** Before any bundle can arrive other than in the APK, reject a version lower than the current one and store the highest version seen. This is recorded as a condition on that future task. |
| S-08 | Low | Content | The JSON is parsed (to read `key_id`) before the signature is checked, so unverified bytes go through the parser. The input is the app's own asset, and kotlinx-serialization only builds a tree. | **Open: accept.** Revisit with S-07 if bundles ever come from outside the APK. |
| S-09 | Low | Store (TH-01) | The Keystore key does not set `setUnlockedDeviceRequired(true)`, so the store can be decrypted while the phone is locked. The 15-minute worker and future sensing need to write while the phone is locked. The device's file-based encryption still protects the data before first unlock. | **Open: accept.** This is a deliberate trade-off for background work. |
| S-10 | Low | Store | `AppSettings.sentOn/markSent` (per-day `sent_<date>` counters) are defined but never called. If wired up as they are, they would keep a notification history in `setting`, which retention never cleans. | **Open: note for T-M5-003/T-M5-008.** When they are wired up, keep only today's counter or add them to retention. |

## Checked and found sound
- **Signature verification:**
  - Ed25519 over the exact bytes, with the pinned key for the manifest's `key_id`.
  - Unknown keys, a bad signature, a count mismatch and a hash mismatch are each rejected.
  - A rejected bundle never replaces the last good one, and non-approved items are filtered even inside a valid bundle.
  - Tested in `CatalogueTest`, plus the Python/Kotlin test vectors.
- **Key handling:**
  - The SQLCipher passphrase is 32 bytes from `SecureRandom`, wrapped with AES-256-GCM in the Android Keystore, with a fresh IV per wrap.
  - No key in code, prefs or Git. `allowBackup=false` and the extraction rules exclude every domain (TC-PR-02, gate checks).
- **Delete-all:**
  - Removes the database with its WAL, SHM and journal files, the wrapped key, the Keystore entry and the cache.
  - Resets the recommender.
- **Network:** no INTERNET permission in any merged manifest, so nothing leaves the device (gate check).
- **Export sharing:** the FileProvider is not exported, is limited to `cache/export/`, and grants read access to the chosen app only.

## Sign-off
- [ ] Tech Lead: S-01 to S-05 fixed (PR for this review); S-06 to S-10 accepted or scheduled.
