package org.pide.bcsr.core.data

/** Retention rules from ARC-04 §1–§2 (PR-06, NFR-03). Pure, so it is unit-tested on the JVM. */
object Retention {
    const val RAW_DAYS = 30L
    const val DERIVED_DAYS = 90L
    const val MAX_BYTES = 50L * 1024 * 1024
    private const val DAY_MS = 86_400_000L

    /** Raw behavioural events (ts column). */
    val RAW_TABLES = listOf("usage_event", "device_event", "activity_event", "notif_count")

    /** Derived data kept 90 days (ts column). Labels, settings and favourites are kept until the user deletes them. */
    val DERIVED_TABLES = listOf("feature_window", "state_estimate", "recommendation")

    /** Every table, for counts, export and delete-all. */
    val ALL_TABLES = RAW_TABLES + DERIVED_TABLES + listOf("label", "favourite", "setting")

    /** The daily job is due after 24 h, or at once if the clock went backwards. */
    fun due(lastRunMs: Long, nowMs: Long) = nowMs - lastRunMs >= DAY_MS || nowMs < lastRunMs

    fun rawCutoff(nowMs: Long) = nowMs - RAW_DAYS * DAY_MS
    fun derivedCutoff(nowMs: Long) = nowMs - DERIVED_DAYS * DAY_MS

    /** DELETE statements the daily job runs, oldest data first. */
    fun deleteStatements(nowMs: Long): List<Pair<String, Long>> =
        RAW_TABLES.map { "DELETE FROM $it WHERE ts < ?" to rawCutoff(nowMs) } +
            DERIVED_TABLES.map { "DELETE FROM $it WHERE ts < ?" to derivedCutoff(nowMs) }
}

/** Table definitions (ARC-04 §1). Package names are never stored, only a salted hash and the category (TH-09). */
internal val SCHEMA = listOf(
    "CREATE TABLE IF NOT EXISTS usage_event (ts INTEGER NOT NULL, ts_end INTEGER, package_hash TEXT, category TEXT)",
    "CREATE TABLE IF NOT EXISTS device_event (ts INTEGER NOT NULL, type TEXT NOT NULL)",
    "CREATE TABLE IF NOT EXISTS activity_event (ts INTEGER NOT NULL, activity TEXT, transition TEXT)",
    "CREATE TABLE IF NOT EXISTS notif_count (ts INTEGER NOT NULL, category TEXT, count INTEGER)",
    "CREATE TABLE IF NOT EXISTS feature_window (ts INTEGER NOT NULL, length_ms INTEGER, json TEXT, gap_flag INTEGER)",
    "CREATE TABLE IF NOT EXISTS state_estimate (ts INTEGER NOT NULL, model_version TEXT, json TEXT)",
    "CREATE TABLE IF NOT EXISTS label (ts INTEGER NOT NULL, valence INTEGER, energy INTEGER, state_id TEXT, source TEXT)",
    "CREATE TABLE IF NOT EXISTS recommendation (ts INTEGER NOT NULL, item_id TEXT, state_id TEXT, policy_version TEXT, feedback TEXT)",
    "CREATE TABLE IF NOT EXISTS favourite (item_id TEXT PRIMARY KEY)",
    "CREATE TABLE IF NOT EXISTS setting (key TEXT PRIMARY KEY, value TEXT)",
)
