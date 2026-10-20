# API Contracts — ARC-06 v0.1

## 1. Internal domain interfaces (Kotlin)
```kotlin
interface StateEstimator {
    suspend fun estimate(window: FeatureWindow): StateEstimate // probs over CS-01..CS-09, modelVersion
}
interface Recommender {
    suspend fun recommend(state: ContextStateId, ctx: RecContext): Recommendation? // approved items only
    suspend fun feedback(recId: String, reward: Feedback)
}
interface InterventionPolicy {
    fun shouldCheckIn(est: StateEstimate, now: Instant, settings: Settings): Decision
}
interface CatalogueRepository {
    fun approvedItemsFor(state: ContextStateId, lang: Lang): List<CatalogueItem>
}
```

## 2. Catalogue bundle format
`catalogue-<version>.json` + `catalogue-<version>.sig` (Ed25519 signature). Schema: `content/schema/catalogue.schema.json`.

## 3. Research ingestion (optional, M5+)
`POST /v1/batches` · body: `{participant_id, app_version, consent_version, windows:[…], labels:[…]}` · TLS 1.3 · 202 Accepted. The full OpenAPI spec is produced in M5 by AG-13.
