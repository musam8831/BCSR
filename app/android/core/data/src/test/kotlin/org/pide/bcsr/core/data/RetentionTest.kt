package org.pide.bcsr.core.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** AC-PR-06: raw events older than 30 days and derived data older than 90 days are deleted; labels are kept. */
class RetentionTest {
    private val now = 100L * 86_400_000L

    @Test fun cutoffs() {
        assertEquals(70L * 86_400_000L, Retention.rawCutoff(now))
        assertEquals(10L * 86_400_000L, Retention.derivedCutoff(now))
    }

    @Test fun exportFieldsAreQuoted() { // T-M5-019 S-04
        assertEquals("", csv(null))
        assertEquals("CS-02", csv("CS-02"))
        assertEquals("\"a,b\"", csv("a,b"))
        assertEquals("\"say \"\"hi\"\"\"", csv("say \"hi\""))
        assertEquals("\"x\ny\"", csv("x\ny"))
    }

    @Test fun oldExportsArePurgedFreshOnesKept() { // T-M5-019 S-03
        val dir = kotlin.io.path.createTempDirectory("export").toFile()
        val old = dir.resolve("bcsr-export-1.txt").apply { writeText("x"); setLastModified(now - 2 * 3_600_000L) }
        val fresh = dir.resolve("bcsr-export-2.txt").apply { writeText("x"); setLastModified(now - 60_000L) }
        val future = dir.resolve("bcsr-export-3.txt").apply { writeText("x"); setLastModified(now + 3_600_000L) }
        BcsrStore.purgeExports(dir, now)
        assertTrue(!old.exists() && fresh.exists() && !future.exists())
        BcsrStore.purgeExports(dir.resolve("missing"), now) // no directory yet: no crash
        dir.deleteRecursively()
    }

    @Test fun onlyKeyFailuresArePermanent() { // T-M5-025: "start fresh" only when the data can never be read
        assertTrue(isPermanentKeyFailure(javax.crypto.AEADBadTagException("tag mismatch")))
        assertTrue(isPermanentKeyFailure(RuntimeException(java.security.UnrecoverableKeyException("gone"))))
        assertTrue(isPermanentKeyFailure(RuntimeException("file is not a database: , while compiling: CREATE TABLE")))
        assertTrue(!isPermanentKeyFailure(java.io.IOException("No space left on device")))
        assertTrue(!isPermanentKeyFailure(IllegalStateException("database is locked")))
        assertTrue(!isPermanentKeyFailure(java.security.ProviderException("Keystore operation failed")))
    }

    @Test fun exportLeavesOutSafetyUse() { // AG-10 R-5, SAF-01 v1.2 §1.5
        assertTrue(!exported("setting", "safety_count") && !exported("setting", "safety_until"))
        assertTrue(exported("setting", "city") && exported("label", "safety_count")) // only setting keys are filtered
    }

    @Test fun dueDaily() {
        assertTrue(Retention.due(0L, now)) // never run
        assertTrue(!Retention.due(now - 3_600_000L, now))
        assertTrue(Retention.due(now - 86_400_000L, now))
        assertTrue(Retention.due(now + 3_600_000L, now)) // clock moved back
    }

    @Test fun statementsCoverRawAndDerivedButNotLabels() {
        val s = Retention.deleteStatements(now)
        assertEquals(Retention.RAW_TABLES.size + Retention.DERIVED_TABLES.size, s.size)
        assertTrue(s.none { it.first.contains("label") || it.first.contains("favourite") || it.first.contains("setting") })
        assertTrue(s.filter { it.first.contains("usage_event") }.all { it.second == Retention.rawCutoff(now) })
        assertTrue(s.filter { it.first.contains("feature_window") }.all { it.second == Retention.derivedCutoff(now) })
    }

    @Test fun schemaHasEveryTableAndNoPackageNames() {
        Retention.ALL_TABLES.forEach { t -> assertTrue(t, SCHEMA.any { it.contains("EXISTS $t ") }) }
        assertTrue(SCHEMA.none { it.contains("package_name") }) // TH-09: hash + category only
    }

    /** AC-PR-01 / AC-PR-04: no column named for message text or location. Opaque json/value columns need inspection (TC-PR-04b). */
    @Test fun noContentOrLocationColumns() {
        assertEquals(emptyList<String>(), SCHEMA.flatMap(::columns).filter(::forbidden))
        assertTrue(listOf("home_lat", "Latitude", "gps", "notif_text", "msg_body", "coord_x", "homeLat", "notifText").all(::forbidden))
        assertEquals(listOf("lat"), columns("CREATE TABLE t (\"lat\" REAL)"))
        assertTrue(listOf("ts", "ts_end", "package_hash", "category", "state_id", "json", "gap_flag", "valence").none(::forbidden))
        assertEquals(listOf("ts", "category", "count"), columns("CREATE TABLE IF NOT EXISTS notif_count (ts INTEGER NOT NULL, category TEXT, count INTEGER)"))
    }

    // shortcut: covers SCHEMA only; when a migration adds ALTER TABLE columns, run them through this check too
    private fun columns(ddl: String) =
        ddl.substringAfter("(").substringBeforeLast(")").split(",").map { it.trim().substringBefore(" ").trim('"', '`', '[', ']') }

    // whole snake_case / camelCase parts only: catches home_lat and homeLat, not "category"
    private fun forbidden(column: String) = column.replace(Regex("([a-z0-9])([A-Z])"), "$1_$2").lowercase().split("_").any {
        it in setOf("lat", "lng", "lon", "latitude", "longitude", "location", "gps", "title", "text", "body", "content", "message", "msg") ||
            it.startsWith("coord")
    }
}
