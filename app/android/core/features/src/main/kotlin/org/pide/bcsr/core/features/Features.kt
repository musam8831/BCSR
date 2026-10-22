package org.pide.bcsr.core.features

import kotlin.math.ln

/** Device event from the data dictionary (ARC-04 device_event). */
data class DeviceEvent(val ts: Long, val type: Type) {
    enum class Type { UNLOCK, SCREEN_ON, SCREEN_OFF }
}

/** One feature window (FS-001): raw values, personal z-scores (null before the baseline exists) and the gap flag. */
data class FeatureWindow(
    val startMs: Long,
    val lengthMs: Long,
    val values: Map<String, Double>,
    val z: Map<String, Double?> = emptyMap(),
    val gapFlag: Boolean = false,
)

const val CHECKING_SESSION_MAX_S = 30L
const val BASELINE_DAYS = 14
const val BASELINE_MIN_DAYS = 7

private fun inWindow(ts: Long, startMs: Long, lengthMs: Long) = ts >= startMs && ts < startMs + lengthMs

/** FS-001 unlock_count: UNLOCK events in [start, start + length). */
fun unlockCount(events: List<DeviceEvent>, startMs: Long, lengthMs: Long): Int =
    events.count { it.type == DeviceEvent.Type.UNLOCK && inWindow(it.ts, startMs, lengthMs) }

/**
 * FS-001 checking_sessions: SCREEN_ON → next SCREEN_OFF sessions that start in the window and last < 30 s.
 * A SCREEN_ON with no SCREEN_OFF before the next SCREEN_ON is not counted (CR-REQ-0002 definition).
 */
fun checkingSessions(events: List<DeviceEvent>, startMs: Long, lengthMs: Long): Int {
    var count = 0
    var onTs: Long? = null
    for (e in events.filter { it.type != DeviceEvent.Type.UNLOCK }.sortedBy { it.ts }) {
        if (e.type == DeviceEvent.Type.SCREEN_ON) {
            onTs = e.ts
        } else if (onTs != null) {
            if (inWindow(onTs, startMs, lengthMs) && e.ts - onTs < CHECKING_SESSION_MAX_S * 1000) count++
            onTs = null
        }
    }
    return count
}

/** FS-001 app_switch_entropy: Shannon entropy (bits) of the foreground-session categories; 0 if none. */
fun appSwitchEntropy(categories: List<String>): Double {
    if (categories.isEmpty()) return 0.0
    val n = categories.size.toDouble()
    return -categories.groupingBy { it }.eachCount().values.sumOf { c -> (c / n) * (ln(c / n) / ln(2.0)) }
}

/** FS-001 z-score: (value − median) / MAD over the trailing 14 daily values; null before 7 days or when MAD is 0. */
fun robustZ(value: Double, history: List<Double>): Double? {
    val hist = history.takeLast(BASELINE_DAYS)
    if (hist.size < BASELINE_MIN_DAYS) return null
    val med = median(hist)
    val mad = median(hist.map { kotlin.math.abs(it - med) })
    return if (mad == 0.0) null else (value - med) / mad
}

private fun median(xs: List<Double>): Double {
    val s = xs.sorted()
    val m = s.size / 2
    return if (s.size % 2 == 1) s[m] else (s[m - 1] + s[m]) / 2
}

/** True if collection missed the window: no heartbeat in it, or two heartbeats more than 2 × [expectedMs] apart (TH-16). */
fun gapFlag(heartbeatsMs: List<Long>, startMs: Long, lengthMs: Long, expectedMs: Long): Boolean {
    val inside = heartbeatsMs.filter { inWindow(it, startMs, lengthMs) }.sorted()
    if (inside.isEmpty()) return true
    return inside.zipWithNext { a, b -> b - a }.any { it > 2 * expectedMs }
}
