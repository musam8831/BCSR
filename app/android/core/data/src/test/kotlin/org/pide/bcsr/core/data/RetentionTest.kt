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
}
