package org.pide.bcsr.core.policy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NotificationPolicyTest {
    private val day = LocalDate.of(2026, 11, 2)
    private val settings = TimingSettings(LocalTime.of(23, 0), LocalTime.of(6, 0), salahBufferMinutes = 15, dailyCap = 4)
    private val salah = listOf("05:10", "12:05", "15:30", "17:20", "18:45").map(LocalTime::parse)
    private fun at(h: Int, m: Int) = day.atTime(h, m)

    @Test
    fun freeTimeIsAllowed() {
        assertEquals(Decision.Allow, decide(at(10, 0), 0, salah, settings))
    }

    @Test
    fun quietHoursAcrossMidnightDeferToMorning() {
        assertEquals(Decision.Defer(day.plusDays(1).atTime(6, 0)), decide(at(23, 30), 0, salah, settings))
        assertEquals(Decision.Defer(at(6, 0)), decide(at(2, 0), 0, salah, settings))
    }

    @Test
    fun salahWindowDefersToBufferEnd() {
        assertEquals(Decision.Defer(at(12, 20)), decide(at(12, 0), 0, salah, settings))
        assertEquals(Decision.Allow, decide(at(12, 20), 0, salah, settings)) // window end is exclusive
    }

    @Test
    fun capReachedDrops() {
        assertEquals(Decision.Drop, decide(at(10, 0), 4, salah, settings))
    }

    @Test
    fun overlappingWindowsAreChained() {
        // Quiet hours end 05:00, inside the Fajr window 04:55–05:25: the deferral chains to 05:25.
        val early = settings.copy(quietEnd = LocalTime.of(5, 0))
        assertEquals(Decision.Defer(at(5, 25)), decide(at(4, 30), 0, salah, early))
    }

    @Test
    fun noQuietHoursWhenStartEqualsEnd() {
        val none = settings.copy(quietStart = LocalTime.MIDNIGHT, quietEnd = LocalTime.MIDNIGHT)
        assertEquals(Decision.Allow, decide(at(2, 0), 0, salah, none))
    }

    /** AC-FR-21: for every minute of the day, an allowed time is never inside quiet hours or a Salah window. */
    @Test
    fun exhaustiveDayNeverAllowsBlockedMinutes() {
        var t: LocalDateTime = day.atStartOfDay()
        while (t.isBefore(day.plusDays(1).atStartOfDay())) {
            val d = decide(t, 0, salah, settings)
            val minute = t.toLocalTime()
            val inQuiet = !minute.isBefore(LocalTime.of(23, 0)) || minute.isBefore(LocalTime.of(6, 0))
            val inSalah = salah.any { s -> !t.isBefore(day.atTime(s).minusMinutes(15)) && t.isBefore(day.atTime(s).plusMinutes(15)) }
            if (d == Decision.Allow) assertTrue("allowed inside a blocked window at $t", !inQuiet && !inSalah)
            if (d is Decision.Defer) assertTrue("deferral must move forward at $t", d.until.isAfter(t))
            t = t.plusMinutes(1)
        }
    }
}
