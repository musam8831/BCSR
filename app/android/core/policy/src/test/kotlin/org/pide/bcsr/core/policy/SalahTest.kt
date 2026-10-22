package org.pide.bcsr.core.policy

import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/** Sanity checks only; exact times come from the Adhan library (RES-03) and are checked against a printed timetable in M5 device tests. */
class SalahTest {
    private val day = LocalDate.of(2026, 11, 2)

    @Test fun fiveTimesInOrderForEveryCity() {
        City.entries.forEach { city ->
            val t = salahTimes(day, city)
            assertTrue("$city $t", t.size == 5 && t.zipWithNext().all { (a, b) -> a.isBefore(b) })
            assertTrue("$city Dhuhr ${t[1]}", t[1].isAfter(LocalTime.of(11, 30)) && t[1].isBefore(LocalTime.of(13, 0)))
            assertTrue("$city Fajr ${t[0]}", t[0].isAfter(LocalTime.of(4, 0)) && t[0].isBefore(LocalTime.of(6, 30)))
        }
    }

    @Test fun hanafiAsrIsLaterThanStandard() {
        val std = salahTimes(day, City.LAHORE, asr = AsrMethod.STANDARD)[2]
        val han = salahTimes(day, City.LAHORE, asr = AsrMethod.HANAFI)[2]
        assertTrue("$std vs $han", han.isAfter(std))
    }

    @Test fun choosingHanafiDoesNotLeakIntoLaterStandardCalls() {
        val before = salahTimes(day, City.KARACHI, asr = AsrMethod.STANDARD)
        salahTimes(day, City.KARACHI, asr = AsrMethod.HANAFI)
        val after = salahTimes(day, City.KARACHI, asr = AsrMethod.STANDARD)
        assertTrue("$before vs $after", before == after)
    }

    @Test fun feedsThePolicy() {
        val times = salahTimes(day, City.ISLAMABAD)
        val s = TimingSettings(LocalTime.of(23, 0), LocalTime.of(6, 0), 15, 4)
        val atDhuhr = day.atTime(times[1])
        assertTrue(decide(atDhuhr, 0, times, s) is Decision.Defer)
    }
}
