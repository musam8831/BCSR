package org.pide.bcsr.spike

import android.app.AppOpsManager
import android.app.PendingIntent
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

// ---------- SPK-01: usage, screen and keyguard events ----------

fun hasUsageAccess(ctx: Context): Boolean {
    val ops = ctx.getSystemService(AppOpsManager::class.java)
    val mode = ops.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.packageName)
    return mode == AppOpsManager.MODE_ALLOWED
}

private val eventNames = mapOf(
    UsageEvents.Event.ACTIVITY_RESUMED to "app_resumed",
    UsageEvents.Event.ACTIVITY_PAUSED to "app_paused",
    UsageEvents.Event.SCREEN_INTERACTIVE to "screen_on",
    UsageEvents.Event.SCREEN_NON_INTERACTIVE to "screen_off",
    UsageEvents.Event.KEYGUARD_SHOWN to "keyguard_shown",
    UsageEvents.Event.KEYGUARD_HIDDEN to "unlock",
)

/** Reads the last [hours] of usage events into usage_events.csv and returns counts per event type. */
fun collectUsageEvents(ctx: Context, hours: Int = 24): Map<String, Int> {
    val usm = ctx.getSystemService(UsageStatsManager::class.java)
    val end = System.currentTimeMillis()
    val events = usm.queryEvents(end - hours * 3_600_000L, end)
    val counts = sortedMapOf<String, Int>()
    val e = UsageEvents.Event()
    CsvLog.file(ctx, "usage_events.csv").delete() // one snapshot per collection
    while (events.hasNextEvent()) {
        events.getNextEvent(e)
        val name = eventNames[e.eventType] ?: continue
        counts[name] = (counts[name] ?: 0) + 1
        val cat = if (name.startsWith("app_")) appCategory(ctx, e.packageName) else ""
        CsvLog.append(ctx, "usage_events.csv", "time_ms,event,category", "${e.timeStamp},$name,$cat")
    }
    return counts
}

// ---------- SPK-03: notification counts (no content) ----------

fun notificationListenerEnabled(ctx: Context): Boolean {
    val flat = Settings.Secure.getString(ctx.contentResolver, "enabled_notification_listeners") ?: return false
    return flat.contains(ComponentName(ctx, NotificationCounter::class.java).flattenToString())
}

class NotificationCounter : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Only time and category; title, text and extras are never read.
        CsvLog.append(this, "notifications.csv", "time_ms,category", "${sbn.postTime},${appCategory(this, sbn.packageName)}")
    }
}

// ---------- SPK-03: activity recognition transitions ----------

private val activityTypes = mapOf(
    DetectedActivity.STILL to "still",
    DetectedActivity.WALKING to "walking",
    DetectedActivity.RUNNING to "running",
    DetectedActivity.ON_BICYCLE to "bicycle",
    DetectedActivity.IN_VEHICLE to "vehicle",
)

fun startActivityUpdates(ctx: Context) {
    val transitions = activityTypes.keys.flatMap { type ->
        listOf(ActivityTransition.ACTIVITY_TRANSITION_ENTER, ActivityTransition.ACTIVITY_TRANSITION_EXIT).map {
            ActivityTransition.Builder().setActivityType(type).setActivityTransition(it).build()
        }
    }
    // Transitions API needs a mutable PendingIntent so Play services can attach the result.
    val pi = PendingIntent.getBroadcast(
        ctx, 0, Intent(ctx, ActivityReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
    )
    try {
        ActivityRecognition.getClient(ctx).requestActivityTransitionUpdates(ActivityTransitionRequest(transitions), pi)
    } catch (e: SecurityException) {
        CsvLog.append(ctx, "errors.csv", "time_ms,error", "${System.currentTimeMillis()},activity_permission_denied")
    }
}

class ActivityReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        val result = ActivityTransitionResult.extractResult(intent) ?: return
        for (ev in result.transitionEvents) {
            val kind = if (ev.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER) "enter" else "exit"
            CsvLog.append(ctx, "activity.csv", "time_ms,activity,transition",
                "${System.currentTimeMillis()},${activityTypes[ev.activityType] ?: "other"},$kind")
        }
    }
}

// ---------- SPK-04 / SPK-05: background heartbeat with battery and standby state ----------

const val HEARTBEAT_MINUTES = 15L

class HeartbeatWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val ctx = applicationContext
        val bm = ctx.getSystemService(BatteryManager::class.java)
        val pm = ctx.getSystemService(PowerManager::class.java)
        val usm = ctx.getSystemService(UsageStatsManager::class.java)
        CsvLog.append(
            ctx, "heartbeat.csv",
            "time_ms,battery_pct,charging,power_save,device_idle,standby_bucket",
            listOf(
                System.currentTimeMillis(),
                bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
                bm.isCharging,
                pm.isPowerSaveMode,
                pm.isDeviceIdleMode,
                usm.appStandbyBucket,
            ).joinToString(","),
        )
        return Result.success()
    }
}
