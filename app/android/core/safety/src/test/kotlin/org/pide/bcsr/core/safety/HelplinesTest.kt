package org.pide.bcsr.core.safety

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class HelplinesTest {
    private val today = LocalDate.of(2026, 11, 1)

    @Test fun releaseShowsOnlyVerifiedAndDevAddsPartly() {
        assertEquals(listOf("1122"), shownHelplines(today, devBuild = false).map { it.number })
        assertEquals(listOf("1122", "1166"), shownHelplines(today, devBuild = true).map { it.number })
    }

    @Test fun notVerifiedIsNeverShown() {
        assertTrue(shownHelplines(today, devBuild = true).none { it.status == Verification.NOT_VERIFIED })
    }

    /** SAF-02: release is blocked until a mental-health line is fully verified. */
    @Test fun currentDirectoryBlocksRelease() {
        assertEquals(listOf("no verified, current mental-health helpline"), releaseBlockers(today))
    }

    /** AC-SR-05: entries older than 90 days stop showing and block release. */
    @Test fun staleEntriesExpire() {
        val later = LocalDate.of(2027, 2, 1)
        assertTrue(shownHelplines(later, devBuild = true).isEmpty())
        assertTrue(releaseBlockers(later).contains("Rescue 1122 not reviewed in 90 days"))
    }

    @Test fun fullyVerifiedDirectoryUnblocks() {
        val good = listOf(
            Helpline("E", "1122", Purpose.EMERGENCY, Verification.VERIFIED, today),
            Helpline("M", "1166", Purpose.MENTAL_HEALTH, Verification.VERIFIED, today),
        )
        assertTrue(releaseBlockers(today, good).isEmpty())
    }
}
