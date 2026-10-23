package org.pide.bcsr

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.pide.bcsr.core.data.BcsrStore
import org.pide.bcsr.core.data.Retention
import org.pide.bcsr.core.inference.ContextState
import org.pide.bcsr.core.inference.StateEstimate
import org.pide.bcsr.core.policy.Decision
import org.pide.bcsr.core.policy.TimingSettings
import org.pide.bcsr.core.policy.decide
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

const val CHECK_IN_THRESHOLD = 0.6 // proposed default for FR-14; tuned against EP-001 S2 in the pilot
const val DAILY_CAP = 4

/**
 * Whether a check-in may be posted now (FR-14, FR-21, SAF-01 §3). Pure, so it is unit-tested.
 * No prompt for a calm estimate, below the threshold, while paused, or within 24 h after the safety screen.
 */
fun mayPostCheckIn(
    estimate: StateEstimate?,
    now: LocalDateTime,
    nowMs: Long,
    sentToday: Int,
    salah: List<LocalTime>,
    quietStart: LocalTime,
    quietEnd: LocalTime,
    paused: Boolean,
    quietAfterSafetyUntil: Long,
): Boolean {
    if (estimate == null || paused || nowMs < quietAfterSafetyUntil) return false
    val top = estimate.top
    if (top == ContextState.CALM || estimate.probs.getValue(top) < CHECK_IN_THRESHOLD) return false
    return decide(now, sentToday, salah, TimingSettings(quietStart, quietEnd, salahBufferMinutes = 15, dailyCap = DAILY_CAP)) == Decision.Allow
}

/**
 * Every 15 minutes: estimate the latest feature window and post a neutral check-in if [mayPostCheckIn] allows.
 * Until :core:sensing exists (T-M5-003, after G2) there are no feature windows, so this posts nothing.
 */
class CheckInWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        // AC-PR-06: the daily retention job rides on this worker (T-M5-019 finding S-01)
        val c = (applicationContext as BcsrApp).container
        val s = c.settings()
        val now = System.currentTimeMillis()
        try {
            if (Retention.due(s.lastRetentionMs, now)) {
                c.store.runRetention(now)
                s.lastRetentionMs = now
            }
        } catch (e: IllegalStateException) {
            return Result.retry() // store closed by a delete-all during this run
        }
        BcsrStore.purgeExports(java.io.File(applicationContext.cacheDir, "export"), now)
        return Result.success() // feature windows arrive with T-M5-003
    }

    companion object {
        fun schedule(context: Context) = WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "check-in", ExistingPeriodicWorkPolicy.KEEP, PeriodicWorkRequestBuilder<CheckInWorker>(15, TimeUnit.MINUTES).build(),
        )

        /** Posts the check-in: a question, no feeling named, private on the lock screen (TH-06). */
        fun post(context: Context) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(NotificationChannel("checkin", context.getString(R.string.notif_channel), NotificationManager.IMPORTANCE_DEFAULT))
            val open = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
            nm.notify(1, NotificationCompat.Builder(context, "checkin")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentText(context.getString(R.string.notif_text))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentIntent(open)
                .setAutoCancel(true)
                .build())
        }
    }
}
