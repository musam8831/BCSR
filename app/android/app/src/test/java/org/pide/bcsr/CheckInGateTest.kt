package org.pide.bcsr

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pide.bcsr.core.inference.ContextState
import org.pide.bcsr.core.inference.StateEstimate
import java.time.LocalDate
import java.time.LocalTime

/** AC-FR-14 / AC-FR-21 / SAF-01 §3 for the check-in gate. */
class CheckInGateTest {
    private val day = LocalDate.of(2026, 11, 2)
    private val salah = listOf("05:10", "12:05", "15:30", "17:20", "18:45").map(LocalTime::parse)
    private fun est(top: ContextState, p: Double) =
        StateEstimate(ContextState.PASSIVE.associateWith { if (it == top) p else (1 - p) / 5 }, "t")
    private fun gate(e: StateEstimate?, h: Int, m: Int = 0, sent: Int = 0, paused: Boolean = false, safetyUntil: Long = 0, nowMs: Long = 1_000) =
        mayPostCheckIn(e, day.atTime(h, m), nowMs, sent, salah, LocalTime.of(23, 0), LocalTime.of(6, 0), paused, safetyUntil)

    @Test fun confidentNonCalmEstimateAtFreeTimeIsAllowed() = assertTrue(gate(est(ContextState.SLEEPLESS, 0.8), 10))

    @Test fun calmOrUnsureIsNotPrompted() {
        assertFalse(gate(est(ContextState.CALM, 0.9), 10))
        assertFalse(gate(est(ContextState.WORRY, 0.5), 10))
        assertFalse(gate(null, 10))
    }

    @Test fun quietHoursSalahAndCapBlock() {
        assertFalse(gate(est(ContextState.WORRY, 0.8), 23, 30))
        assertFalse(gate(est(ContextState.WORRY, 0.8), 12, 10))
        assertFalse(gate(est(ContextState.WORRY, 0.8), 10, sent = DAILY_CAP))
    }

    @Test fun pausedOrAfterSafetyScreenBlocks() {
        assertFalse(gate(est(ContextState.WORRY, 0.8), 10, paused = true))
        assertFalse(gate(est(ContextState.WORRY, 0.8), 10, safetyUntil = 5_000, nowMs = 1_000))
    }
}
