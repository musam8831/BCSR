# API Contracts — ARC-06 v1.0-rc

## 1. Internal domain interfaces (Kotlin)
```kotlin
interface StateEstimator {
    suspend fun estimate(window: FeatureWindow): StateEstimate // probs over CS-01..CS-04, CS-06, CS-08; modelVersion
}
interface Recommender {
    suspend fun recommend(state: ContextStateId, ctx: RecContext): Recommendation? // approved items only
    suspend fun feedback(recId: String, reward: Feedback)
}
interface InterventionPolicy {
    fun shouldCheckIn(est: StateEstimate, now: Instant, settings: Settings): Decision
}
interface CatalogueRepository {
    fun approvedItemsFor(state: ContextStateId, lang: Lang): List<CatalogueItem> // drops any non-approved item (ADR-0005 §6)
}
```

## 2. Catalogue bundle format (ADR-0005)
`catalogue-<version>.json` + `catalogue-<version>.sig` (Ed25519 over the exact JSON bytes). Items follow `content/schema/catalogue.schema.json`.

```json
{
  "manifest": {
    "version": "2026.1",
    "created": "2026-12-01T00:00:00Z",
    "item_count": 60,
    "items_sha256": "<hex of SHA-256 over the items array>",
    "key_id": "bcsr-catalogue-2026"
  },
  "items": [ /* approved items only, sorted by id */ ]
}
```
Load rules: verify the signature with the pinned public key for `key_id`; check `item_count` and `items_sha256`; on any failure keep the last good bundle. The runtime filter still drops non-approved items.

## 3. Research ingestion (optional, M5+)
`POST /v1/batches` · body: `{participant_id, app_version, consent_version, windows:[…], labels:[…]}` · TLS 1.3 · 202 Accepted. The full OpenAPI spec is produced in M5 by AG-13.
