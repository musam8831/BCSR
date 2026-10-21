package org.pide.bcsr.spike

import android.content.Context
import android.content.pm.ApplicationInfo
import java.io.File

/** Append-only CSV files in the app's external files dir; pulled with `adb pull` (see SPK protocols). */
object CsvLog {
    fun file(ctx: Context, name: String): File = File(ctx.getExternalFilesDir(null), name)

    @Synchronized
    fun append(ctx: Context, name: String, header: String, row: String) {
        val f = file(ctx, name)
        if (!f.exists()) f.writeText(header + "\n")
        f.appendText(row + "\n")
    }

    fun readColumn(ctx: Context, name: String, col: Int): List<String> {
        val f = file(ctx, name)
        if (!f.exists()) return emptyList()
        return f.readLines().drop(1).mapNotNull { it.split(',').getOrNull(col) }
    }
}

/** App category name only, never the package name, to stay close to PR-01 even in a spike. */
fun appCategory(ctx: Context, pkg: String): String = try {
    when (ctx.packageManager.getApplicationInfo(pkg, 0).category) {
        ApplicationInfo.CATEGORY_GAME -> "game"
        ApplicationInfo.CATEGORY_AUDIO -> "audio"
        ApplicationInfo.CATEGORY_VIDEO -> "video"
        ApplicationInfo.CATEGORY_IMAGE -> "image"
        ApplicationInfo.CATEGORY_SOCIAL -> "social"
        ApplicationInfo.CATEGORY_NEWS -> "news"
        ApplicationInfo.CATEGORY_MAPS -> "maps"
        ApplicationInfo.CATEGORY_PRODUCTIVITY -> "productivity"
        else -> "other"
    }
} catch (e: Exception) {
    "unknown"
}
