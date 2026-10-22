package org.pide.bcsr.core.features

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pide.bcsr.core.features.DeviceEvent.Type.SCREEN_OFF
import org.pide.bcsr.core.features.DeviceEvent.Type.SCREEN_ON
import org.pide.bcsr.core.features.DeviceEvent.Type.UNLOCK

/** Same fixtures and expected values as ml/features/test_fs001.py (parity, T-M5-004). */
class FeaturesParityTest {
    private val min = 60_000L
    private fun ev(minute: Long, t: DeviceEvent.Type, sec: Long = 0) = DeviceEvent(minute * min + sec * 1000, t)

    @Test fun unlockCountRespectsWindowEdges() {
        val events = listOf(ev(0, UNLOCK), ev(14, UNLOCK), ev(15, UNLOCK), ev(5, SCREEN_ON))
        assertEquals(2, unlockCount(events, 0, 15 * min))
        assertEquals(1, unlockCount(events, 15 * min, 15 * min))
    }

    @Test fun checkingSessions() {
        val events = listOf(
            ev(1, SCREEN_ON), ev(1, SCREEN_OFF, 20),
            ev(2, SCREEN_ON), ev(3, SCREEN_OFF),
            ev(4, SCREEN_ON), ev(4, SCREEN_OFF, 30),
            ev(5, SCREEN_ON), ev(6, SCREEN_ON),
            ev(6, SCREEN_OFF, 5),
        )
        assertEquals(2, checkingSessions(events, 0, 60 * min))
    }

    @Test fun entropy() {
        assertEquals(0.0, appSwitchEntropy(emptyList()), 1e-12)
        assertEquals(0.0, appSwitchEntropy(List(5) { "SOCIAL" }), 1e-12)
        assertEquals(1.0, appSwitchEntropy(listOf("SOCIAL", "VIDEO")), 1e-12)
        assertEquals(2.0, appSwitchEntropy(listOf("A", "B", "C", "D")), 1e-12)
    }

    @Test fun robustZNeedsSevenDaysAndSpread() {
        assertNull(robustZ(5.0, listOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)))
        assertNull(robustZ(5.0, List(10) { 3.0 }))
        assertEquals(3.0, robustZ(14.0, listOf(10.0, 12.0, 11.0, 13.0, 9.0, 10.0, 12.0, 11.0))!!, 1e-12)
    }

    @Test fun robustZUsesOnlyTrailing14Days() {
        val recent = listOf(10.0, 12.0, 11.0, 13.0, 9.0, 10.0, 12.0, 11.0, 10.0, 12.0, 11.0, 13.0, 9.0, 10.0)
        assertEquals(robustZ(11.0, recent)!!, robustZ(11.0, List(20) { 1000.0 } + recent)!!, 1e-12)
    }

    @Test fun gapFlagMarksMissedCollection() {
        val m15 = 15 * min
        assertFalse(gapFlag(listOf(0, m15, 2 * m15, 3 * m15), 0, 60 * min, m15))
        assertTrue(gapFlag(listOf(0, 3 * m15), 0, 60 * min, m15))
        assertTrue(gapFlag(emptyList(), 0, 60 * min, m15))
    }
}
