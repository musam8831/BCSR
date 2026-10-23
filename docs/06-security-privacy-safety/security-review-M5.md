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
| S-01 | High | Store (AC-PR-06) | `BcsrStore.runRetention` was never called. Raw events past 30 days and derived data past 90 days would never be deleted, and the store would never be held to 50 MB. | **Fixed.** `CheckInWorker` runs it once a day, using `Retention.due`, which is unit-tested and handles the clock moving backwards. The last-run time is kept in `setting.retention_at`. If a delete-all closes the store mid-run, the worker retries. The worker path itself is not unit-tested; the device check is TC-PR-06b (T-M5-014). |
| S-02 | Medium | Store (TH-01, availability) | The wrapped database passphrase was written in place. A crash or power cut mid-write would leave a truncated key file, so the database could not be decrypted on any later start (see S-06). | **Fixed.** The key goes to a temporary file, which is synced to disk (`fd.sync()`) before the rename. This covers process crashes and power loss on journaling file systems. It is not unit-tested: it needs the Keystore, so a device. |
| S-03 | Medium | Export (TH-10) | The plaintext export in `cache/export/` stayed until delete-all. A copy of the user's whole history sat outside the encrypted store indefinitely, and a fixed file name meant a second export overwrote the first while it might still be read. | **Fixed.** Each export gets a timestamped name. Exports older than 1 hour are deleted at app start and on every worker run (about every 15 minutes), so a fresh share is never removed while the receiving app reads it. A plaintext copy lasts about an hour, longer if the system defers the worker (Doze, battery saver, standby). Files with a future modification time (the clock moved back) are purged too. Delete-all clears everything. `purgeExports` is unit-tested. |
| S-04 | Low | Export (integrity) | Export fields were not escaped. A comma or line break in a value would shift the columns, so the user's copy of their data would be misread. | **Fixed.** RFC 4180 quoting (`csv()`), unit-tested. Spreadsheet formula prefixes (`=`, `+`, `-`, `@`) are not neutralised; no free text is exported today. Revisit when free-text labels exist. |
| S-05 | Low | Store (injection) | `record()` checked the table against an allowlist but put the column names into SQL unchecked. All callers today pass constants. | **Fixed.** Column names must fully match `[a-z_]+`, and the column and value counts must agree. Rejection is not unit-tested (`BcsrStore` needs a device); add it to the `:core:data` androidTest with T-M5-014. |
| S-06 | **High** | Store (availability, AC-SR-01) | If the Keystore key is lost or invalidated (OEM bugs, restore to another device) or the key file is corrupt, `passphrase()` throws inside `Application.onCreate`. The app then crashes before any screen, **including the safety screen and helplines** (FR-24). | **Fixed in code (T-M5-025, PR #31); device evidence and HC-06 safety sign-off pending.** Any failure to build the app's container leaves it null instead of crashing, so the **Locked** screen shows, with the helplines (`HelplineButtons`, shared with the safety screen and gate-checked) and the not-medical line. Two cases:
  - **The key is gone or does not fit** (GCM tag failure, invalidated or unrecoverable key, short key file, "file is not a database"): Locked offers **Start fresh**, which runs delete-all and reopens.
  - **Anything else** (disk full, a locked database, a passing Keystore error): Locked offers only **Try again**, so readable data is never offered for deletion.

  Also fixed: stored feedback with an unknown value is skipped instead of crashing; Privacy-Center delete-all goes through the same guarded path; the worker skips work while locked.

  Tests: `onlyKeyFailuresArePermanent` (JVM), and the androidTest `corruptKeyIsUnavailableThenStartFreshWorks`, which runs with T-M5-014. |
| S-13 | Low | Delete-all (AC-FR-25) | `startFresh` swallows a delete-all failure, so the UI moves to onboarding even if some files remain. The cache is now cleared before the Keystore step, which can throw, and failures are logged to logcat only. | **Open: schedule with T-M5-020 (UX).** Check that the files are gone and tell the user if deletion was incomplete. On the Locked screen, offer start fresh with honest copy after repeated "Try again" failures. Pin the real SQLCipher error text with a T-M5-014 device case. |
| S-07 | Low | Content (TH-02) | Bundle rollback: `CatalogueRepository.offer` accepts any validly signed bundle, including an older one that still holds an item the scholar later withdrew. Today the only bundle is the one shipped in the APK, so there is no path. | **Open: accept for now.** Before any bundle can arrive other than in the APK, reject a version lower than the current one and store the highest version seen. This is recorded as a condition on that future task. |
| S-08 | Medium | Content (availability) | `load()` read the manifest and items with throwing accessors (`jsonPrimitive`, `jsonObject`), some before the signature check. A malformed bundle threw instead of being rejected, which crashed the app at start. A malformed `.pub` key asset did the same. | **Fixed.** `load()` is total: any exception becomes `Rejected("malformed bundle: …")`, unit-tested with four bad shapes. A key file that fails to parse is skipped, so bundles signed with it are rejected. |
| S-09 | Low | Store (TH-01) | The Keystore key does not set `setUnlockedDeviceRequired(true)`, so the store can be decrypted while the phone is locked. The 15-minute worker and future sensing need to write while the phone is locked. The device's file-based encryption still protects the data before first unlock. | **Open: accept.** This is a deliberate trade-off for background work. |
| S-10 | Low | Store | `AppSettings.sentOn/markSent` (per-day `sent_<date>` counters) are defined but never called. If wired up as they are, they would keep a notification history in `setting`, which retention never cleans. | **Open: note for T-M5-003/T-M5-008.** When they are wired up, keep only today's counter or add them to retention. |
| S-11 | Low | App (availability) | The Privacy Center counts rows and writes the export on the main thread, and the worker's VACUUM holds the single connection. With a large store, the UI can stall or show an ANR. | **Partly fixed (PR #33).** Moved off the main thread: the Privacy Center counts and export (with failures caught; a failed export shows a message) and the safety screen's open counter and 24 h quiet write, so the helplines never wait for a VACUUM. Still on the main thread, to move with T-M5-003 when real data volume arrives: the check-in label insert, the recommendation read and insert, feedback, favourites, the pause switch, and delete-all, which closes and reopens the store and replays recommendations. |
| S-12 | Low | Content / store (nits) | Duplicate item IDs inside a signed bundle are not rejected; the build script rejects them first. The SQLCipher passphrase `ByteArray` is not zeroed after the database opens. | **Open: accept**, and revisit with S-07. Also revisit then: `load()` catches `Exception`, not `StackOverflowError` from deeply nested JSON. |

## Checked and found sound
- **Signature verification:**
  - Ed25519 over the exact bytes, with the pinned key for the manifest's `key_id`.
  - Unknown keys, a bad signature, a count mismatch, a hash mismatch and (since S-08) a malformed shape are each rejected.
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
- [ ] Tech Lead: S-01 to S-05 and S-08 fixed (PR #30). S-06 is **not** for acceptance: it is scheduled as T-M5-025 before any tester build. S-07, S-09 and S-12 accepted; S-10 and S-11 scheduled with T-M5-003/T-M5-008.
