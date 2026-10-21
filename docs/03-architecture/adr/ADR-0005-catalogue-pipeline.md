# ADR-0005: Signed, approved-only catalogue pipeline

- Status: Proposed (accept at G2)
- Date: 2026-10-21
- Task: T-M2-008 · Owners: AG-03 + AG-08 · Reviewer: AG-09
- Related: ADR-0004, CR-01..CR-07, FR-15, TH-02, TH-14, api-contracts §2

## Context
Religious content must reach the app only when a named scholar has approved it (AGENTS.md §2, CR-02), and must not be alterable in transit or on the device (TH-02). The recommender must never return anything else (FR-15).

## Decision
1. **Source:** one JSON file per item in `content/catalogue/items/`, validated against `content/schema/catalogue.schema.json`.
2. **CI gate** (T-M3-005): schema valid; every `approved` item has a review record `RR-NNNN.md` with a named reviewer; reference format checks; duplicate detection. A failure blocks the merge.
3. **Bundle build:** a script selects **only** items with `verification.status = approved`, sorts them by ID, and writes `catalogue-<version>.json` with a manifest (§2 of api-contracts). Draft, in-review and rejected items are never written into a bundle.
4. **Signing:** the bundle is signed with **Ed25519** by a key held **offline by the Project Lead** (never in CI or Git). The signature is `catalogue-<version>.sig`.
5. **App:** ships with the public key pinned. On load it verifies the signature and checks that the manifest's item count and SHA-256 match. Any failure → keep the last good bundle, log a local error, and never load a partial catalogue.
6. **Runtime filter (defence in depth):** `CatalogueRepository.approvedItemsFor()` drops any item whose status is not `approved`, even inside a valid bundle; AC-FR-15 property-tests this.
7. **Updates:** a new bundle ships with an app update in the MVP (no network fetch, NFR-01). An edited item returns to `in_review` (CON-03) and drops out of the next bundle until re-approved.
8. **Key rotation:** a new key is introduced by an app update that pins both keys for one release, then the old key is removed.

## Consequences
- Content cannot change without both a scholar's approval record and the Project Lead's offline signature.
- Content updates need an app release; acceptable for the MVP, revisit with a signed remote fetch after M10.
- The Project Lead's signing key is a single point of failure: back it up offline (TH-14).
