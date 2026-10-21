package org.pide.bcsr.spike

import org.junit.Assert.assertEquals
import org.junit.Test

class GapsTest {
    private val min15 = 15 * 60_000L

    @Test
    fun regularHeartbeatsHaveNoGaps() {
        val times = (0 until 5).map { it * min15 }
        val s = heartbeatGaps(times, min15)
        assertEquals(5, s.heartbeats)
        assertEquals(4, s.intervals)
        assertEquals(0, s.gaps)
        assertEquals(0.0, s.gapRate, 1e-9)
    }

    @Test
    fun lateRunWithinToleranceIsNotAGap() {
        val s = heartbeatGaps(listOf(0L, min15 + 10 * 60_000L), min15) // 25 min < 30 min limit
        assertEquals(0, s.gaps)
    }

    @Test
    fun missedRunsCountAsGapsAndUnsortedInputIsHandled() {
        val s = heartbeatGaps(listOf(4 * min15, 0L, min15), min15) // 0, 15, 60 min
        assertEquals(1, s.gaps)
        assertEquals(3 * min15, s.maxGapMs)
        assertEquals(0.5, s.gapRate, 1e-9)
    }

    @Test
    fun emptyAndSingleInputsAreSafe() {
        assertEquals(0, heartbeatGaps(emptyList(), min15).intervals)
        assertEquals(0.0, heartbeatGaps(listOf(5L), min15).gapRate, 1e-9)
    }
}
