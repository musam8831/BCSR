package org.pide.bcsr.core.policy

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

/** User and app settings that limit when BCSR may notify (FR-21). */
data class TimingSettings(
    val quietStart: LocalTime,
    val quietEnd: LocalTime,
    /** Minutes kept free before and after each Salah time. */
    val salahBufferMinutes: Long,
    val dailyCap: Int,
)

sealed interface Decision {
    data object Allow : Decision
    /** Not now; earliest allowed time today or tomorrow. */
    data class Defer(val until: LocalDateTime) : Decision
    /** Daily cap reached; nothing more today. */
    data object Drop : Decision
}

/**
 * Decides whether a notification may be shown at [now] (AC-FR-21).
 * [salahTimes] are today's computed Salah times (from the prayer-time library, RES-03); [sentToday] counts notifications already shown today.
 */
fun decide(now: LocalDateTime, sentToday: Int, salahTimes: List<LocalTime>, s: TimingSettings): Decision {
    require(s.dailyCap >= 0 && s.salahBufferMinutes >= 0) { "cap and buffer must be non-negative" }
    if (sentToday >= s.dailyCap) return Decision.Drop
    val blockedUntil = blockedUntil(now, salahTimes, s) ?: return Decision.Allow
    return Decision.Defer(blockedUntil)
}

/** End of the blocking window that contains [now], or null if [now] is free. Chains overlapping windows. */
private fun blockedUntil(now: LocalDateTime, salahTimes: List<LocalTime>, s: TimingSettings): LocalDateTime? {
    var t = now
    var moved = false
    repeat(salahTimes.size + 2) { // each pass can only move past one window
        val end = windowEndContaining(t, salahTimes, s) ?: return if (moved) t else null
        t = end
        moved = true
    }
    return t
}

private fun windowEndContaining(t: LocalDateTime, salahTimes: List<LocalTime>, s: TimingSettings): LocalDateTime? {
    quietEnd(t, s)?.let { return it }
    val buffer = Duration.ofMinutes(s.salahBufferMinutes)
    for (dayOffset in -1L..1L) { // windows near midnight can belong to the previous or next day
        val day = t.toLocalDate().plusDays(dayOffset)
        for (salah in salahTimes) {
            val at = day.atTime(salah)
            if (!t.isBefore(at.minus(buffer)) && t.isBefore(at.plus(buffer))) return at.plus(buffer)
        }
    }
    return null
}

/** If [t] is inside quiet hours, the moment they end; quiet hours may cross midnight (e.g. 23:00–06:00). */
private fun quietEnd(t: LocalDateTime, s: TimingSettings): LocalDateTime? {
    if (s.quietStart == s.quietEnd) return null // no quiet hours configured
    val time = t.toLocalTime()
    val crossesMidnight = s.quietStart.isAfter(s.quietEnd)
    val inside = if (crossesMidnight) !time.isBefore(s.quietStart) || time.isBefore(s.quietEnd)
    else !time.isBefore(s.quietStart) && time.isBefore(s.quietEnd)
    if (!inside) return null
    val endDay = if (crossesMidnight && !time.isBefore(s.quietStart)) t.toLocalDate().plusDays(1) else t.toLocalDate()
    return endDay.atTime(s.quietEnd)
}
