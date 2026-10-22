package org.pide.bcsr.core.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

/** AC-PR-02 and AC-FR-25 on a device or emulator (TST-01 instrumented level; not run in CI yet). */
@RunWith(AndroidJUnit4::class)
class EncryptionTest {
    private val ctx = InstrumentationRegistry.getInstrumentation().targetContext

    @Test fun databaseFileIsNotPlainSqlite() {
        val store = BcsrStore.open(ctx)
        store.record("label", listOf("ts", "valence", "energy", "state_id", "source"), listOf(1L, 3, 3, "CS-02", "EMA"))
        store.close()
        val header = ctx.getDatabasePath(BcsrStore.DB_NAME).readBytes().copyOfRange(0, 16).toString(Charsets.US_ASCII)
        assertFalse(header.startsWith("SQLite format 3"))
        BcsrStore.deleteEverything(ctx, null)
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
