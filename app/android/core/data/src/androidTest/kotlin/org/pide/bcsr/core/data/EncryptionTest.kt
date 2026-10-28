package org.pide.bcsr.core.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** AC-PR-02, AC-FR-25, AC-PR-06 on an emulator in CI (`android-instrumented`, T-M5-026) and on the device matrix (T-M5-014). */
@RunWith(AndroidJUnit4::class)
class EncryptionTest {
    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    @org.junit.Before fun cleanStore() = BcsrStore.deleteEverything(ctx, null) // a failed earlier test must not leak state

    @Test fun databaseFileIsNotPlainSqlite() {
        val store = BcsrStore.open(ctx)
        store.record("label", listOf("ts", "valence", "energy", "state_id", "source"), listOf(1L, 3, 3, "CS-02", "EMA"))
        store.close()
        val header = ctx.getDatabasePath(BcsrStore.DB_NAME).readBytes().copyOfRange(0, 16).toString(Charsets.US_ASCII)
        assertFalse(header.startsWith("SQLite format 3"))
        // AC-PR-02: the key is held in the Android Keystore, and a copy cannot be opened with any other key
        assertTrue(java.security.KeyStore.getInstance("AndroidKeyStore").apply { load(null) }.containsAlias("bcsr-db-key"))
        assertThrows(Exception::class.java) {
            net.zetetic.database.sqlcipher.SQLiteDatabase.openOrCreateDatabase(ctx.getDatabasePath(BcsrStore.DB_NAME), ByteArray(32), null, null)
                .use { db -> db.rawQuery("SELECT count(*) FROM sqlite_master", arrayOf<String>()).use { it.moveToFirst() } }
        }
        BcsrStore.deleteEverything(ctx, null)
    }

    /** T-M5-025: a corrupt wrapped key gives StoreUnavailable (not a crash), and start fresh recovers. */
    @Test fun corruptKeyIsUnavailableThenStartFreshWorks() {
        BcsrStore.open(ctx).close()
        java.io.File(ctx.filesDir, "bcsr-db.key").writeBytes(ByteArray(5))
        assertTrue(assertThrows(StoreUnavailable::class.java) { BcsrStore.open(ctx) }.permanent) // short key file: offer start fresh
        BcsrStore.deleteEverything(ctx, null)
        val fresh = BcsrStore.open(ctx)
        assertEquals(0L, fresh.counts().values.sum())
        BcsrStore.deleteEverything(ctx, fresh)
    }

    /** SAF-01 v1.2 §1.5 / AG-10 R-5: the export leaves out safety-screen use, keeps the rest. */
    @Test fun exportOmitsSafetySettings() {
        val store = BcsrStore.open(ctx)
        store.putSetting("safety_count", "2"); store.putSetting("safety_until", "123"); store.putSetting("city", "LAHORE")
        val text = store.exportTo(java.io.File(ctx.cacheDir, "test-export.txt")).readText()
        assertFalse(text.contains("safety_count")); assertFalse(text.contains("safety_until"))
        assertTrue(text.contains("city,LAHORE"))
        BcsrStore.deleteEverything(ctx, store)
    }

    /** AC-PR-06 (TC-PR-06b, age part): raw rows past 30 days and derived rows past 90 days go; labels stay. */
    @Test fun retentionDeletesOldRows() {
        val store = BcsrStore.open(ctx)
        val now = System.currentTimeMillis(); val day = 86_400_000L
        listOf(now - 31 * day, now - day).forEach { store.record("device_event", listOf("ts", "type"), listOf(it, "unlock")) }
        listOf(now - 91 * day, now - 31 * day).forEach {
            store.record("feature_window", listOf("ts", "length_ms", "json", "gap_flag"), listOf(it, 1L, "{}", 0))
        }
        store.record("label", listOf("ts", "valence", "energy", "state_id", "source"), listOf(now - 200 * day, 3, 3, "CS-01", "EMA"))
        store.runRetention(now)
        val c = store.counts()
        assertEquals(1L, c["device_event"]); assertEquals(1L, c["feature_window"]); assertEquals(1L, c["label"])
        BcsrStore.deleteEverything(ctx, store)
    }

    @Test fun deleteEverythingEmptiesTheStore() {
        val store = BcsrStore.open(ctx)
        store.putSetting("x", "y")
        BcsrStore.deleteEverything(ctx, store)
        assertFalse(ctx.getDatabasePath(BcsrStore.DB_NAME).exists())
        val fresh = BcsrStore.open(ctx)
        assertEquals(0L, fresh.counts().values.sum())
        BcsrStore.deleteEverything(ctx, fresh)
    }
}
