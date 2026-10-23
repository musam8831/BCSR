package org.pide.bcsr.core.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import net.zetetic.database.sqlcipher.SQLiteDatabase
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Encrypted on-device store (PR-02, TH-01). The SQLCipher passphrase is 32 random bytes, kept only wrapped by an
 * AES-GCM key in the Android Keystore; deleting that key makes any leftover copy of the database unreadable (ARC-04 §3).
 */
class BcsrStore private constructor(private val context: Context, private val db: SQLiteDatabase) {

    fun record(table: String, columns: List<String>, values: List<Any?>) {
        require(table in Retention.ALL_TABLES) { "unknown table $table" }
        require(columns.all { COLUMN.matches(it) } && columns.size == values.size) { "bad columns $columns" } // names go into SQL
        val sql = "INSERT INTO $table (${columns.joinToString()}) VALUES (${columns.joinToString { "?" }})"
        db.execSQL(sql, values.toTypedArray())
    }

    fun setting(key: String): String? =
        db.rawQuery("SELECT value FROM setting WHERE key = ?", arrayOf(key)).use { if (it.moveToFirst()) it.getString(0) else null }

    fun putSetting(key: String, value: String) =
        db.execSQL("INSERT OR REPLACE INTO setting (key, value) VALUES (?, ?)", arrayOf<Any?>(key, value))

    fun favourites(): Set<String> =
        db.rawQuery("SELECT item_id FROM favourite", arrayOf<String>()).use { c -> buildSet { while (c.moveToNext()) add(c.getString(0)) } }

    fun setFavourite(itemId: String, on: Boolean) = if (on) {
        db.execSQL("INSERT OR IGNORE INTO favourite (item_id) VALUES (?)", arrayOf<Any?>(itemId))
    } else {
        db.execSQL("DELETE FROM favourite WHERE item_id = ?", arrayOf<Any?>(itemId))
    }

    /** (ts, itemId, stateId, feedback) for rebuilding the bandit and the 24-hour no-repeat rule. */
    fun recommendations(): List<RecommendationRow> =
        db.rawQuery("SELECT ts, item_id, state_id, feedback FROM recommendation ORDER BY ts", arrayOf<String>()).use { c ->
            buildList { while (c.moveToNext()) add(RecommendationRow(c.getLong(0), c.getString(1), c.getString(2), c.getString(3))) }
        }

    fun setFeedback(ts: Long, itemId: String, feedback: String) =
        db.execSQL("UPDATE recommendation SET feedback = ? WHERE ts = ? AND item_id = ?", arrayOf<Any?>(feedback, ts, itemId))

    /** Row counts per table for the Privacy Center (UX-05, TH-03). */
    fun counts(): Map<String, Long> = Retention.ALL_TABLES.associateWith { t ->
        db.rawQuery("SELECT COUNT(*) FROM $t", arrayOf<String>()).use { if (it.moveToFirst()) it.getLong(0) else 0L }
    }

    /** Daily retention job (ARC-04 §2): age limits first, then the 50 MB cap by dropping the oldest raw events. */
    fun runRetention(nowMs: Long) {
        Retention.deleteStatements(nowMs).forEach { (sql, cutoff) -> db.execSQL(sql, arrayOf<Any?>(cutoff)) }
        val file = context.getDatabasePath(DB_NAME)
        var guard = 0
        while (file.length() > Retention.MAX_BYTES && guard++ < 10) {
            Retention.RAW_TABLES.forEach { t ->
                db.execSQL("DELETE FROM $t WHERE rowid IN (SELECT rowid FROM $t ORDER BY ts LIMIT 1000)")
            }
            db.execSQL("VACUUM")
        }
    }

    /** Plain-text export of every table for the user (FR-25), written to the app cache for the share sheet only (TH-10). */
    fun exportTo(file: File): File {
        file.bufferedWriter().use { w ->
            Retention.ALL_TABLES.forEach { t ->
                w.write("## $t\n")
                db.rawQuery("SELECT * FROM $t", arrayOf<String>()).use { c ->
                    w.write(c.columnNames.joinToString(",") + "\n")
                    while (c.moveToNext()) w.write((0 until c.columnCount).joinToString(",") { csv(c.getString(it)) } + "\n")
                }
            }
        }
        return file
    }

    fun close() = db.close()

    companion object {
        const val DB_NAME = "bcsr.db"
        private const val KEY_ALIAS = "bcsr-db-key"
        private const val WRAPPED_FILE = "bcsr-db.key"
        private val COLUMN = Regex("[a-z_]+")

        fun open(context: Context): BcsrStore {
            System.loadLibrary("sqlcipher")
            val file = context.getDatabasePath(DB_NAME).apply { parentFile?.mkdirs() }
            val db = SQLiteDatabase.openOrCreateDatabase(file, passphrase(context), null, null)
            SCHEMA.forEach(db::execSQL)
            return BcsrStore(context, db)
        }

        /**
         * Delete-all (ARC-04 §3, AC-FR-25): close, delete the database and its journal files, the wrapped passphrase
         * and the Keystore key. Callers also clear their own caches and return to onboarding.
         */
        /** TH-10 (S-03): delete exports older than [maxAgeMs]; a fresh one may still be read by the app it was shared with. */
        fun purgeExports(dir: File, nowMs: Long, maxAgeMs: Long = 3_600_000L) =
            dir.listFiles()?.filter { nowMs - it.lastModified() > maxAgeMs }?.forEach { it.delete() }

        fun deleteEverything(context: Context, store: BcsrStore?) {
            store?.close()
            val f = context.getDatabasePath(DB_NAME)
            listOf(f, File(f.path + "-wal"), File(f.path + "-shm"), File(f.path + "-journal"), File(context.filesDir, WRAPPED_FILE))
                .forEach { it.delete() }
            KeyStore.getInstance("AndroidKeyStore").apply { load(null) }.deleteEntry(KEY_ALIAS)
            context.cacheDir.listFiles()?.forEach { it.deleteRecursively() }
        }

        private fun passphrase(context: Context): ByteArray {
            val wrapped = File(context.filesDir, WRAPPED_FILE)
            val key = keystoreKey()
            if (wrapped.exists()) {
                val bytes = wrapped.readBytes()
                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, bytes.copyOfRange(0, 12)))
                return cipher.doFinal(bytes.copyOfRange(12, bytes.size))
            }
            val pass = ByteArray(32).also { SecureRandom().nextBytes(it) }
            val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply { init(Cipher.ENCRYPT_MODE, key) }
            // write then rename, so a crash mid-write cannot leave a half key that locks the database for good
            val tmp = File(context.filesDir, "$WRAPPED_FILE.tmp")
            java.io.FileOutputStream(tmp).use { it.write(cipher.iv + cipher.doFinal(pass)); it.fd.sync() } // on disk before the rename
            check(tmp.renameTo(wrapped)) { "could not store the database key" }
            return pass
        }

        private fun keystoreKey(): SecretKey {
            val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            (ks.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }
            return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
                init(
                    KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build(),
                )
            }.generateKey()
        }
    }
}

/** RFC 4180 field: quoted when it holds a comma, quote or line break, so free text cannot shift columns in the export. */
internal fun csv(v: String?): String = when {
    v == null -> ""
    v.any { it == ',' || it == '"' || it == '\n' || it == '\r' } -> "\"" + v.replace("\"", "\"\"") + "\""
    else -> v
}

data class RecommendationRow(val ts: Long, val itemId: String, val stateId: String, val feedback: String?)
