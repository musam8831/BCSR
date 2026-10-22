package org.pide.bcsr.core.inference

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pide.bcsr.core.features.FeatureWindow
import java.util.Random

class ColdStartRulesTest {
    private val model = ColdStartRules()
    private fun win(values: Map<String, Double> = emptyMap(), z: Map<String, Double?> = emptyMap(), gap: Boolean = false) =
        FeatureWindow(0, 3_600_000, values, z, gap)

    /** AC-FR-10: a probability vector over CS-01..04, 06, 08 that sums to 1; nothing for CS-05/07/09/10. */
    @Test fun outputsOnlyPassiveStatesSummingToOne() {
        val rng = Random(10)
        repeat(1000) {
            val e = model.estimate(win(
                mapOf("night_screen_min" to rng.nextDouble() * 120, "longest_session_min" to rng.nextDouble() * 200,
                    "longest_productivity_min" to rng.nextDouble() * 300),
                mapOf("unlock_count" to rng.nextGaussian() * 3, "checking_sessions" to null, "mobility_still_ratio" to rng.nextGaussian()),
                gap = rng.nextBoolean(),
            ))
            assertEquals(ContextState.PASSIVE.toSet(), e.probs.keys)
            assertEquals(1.0, e.probs.values.sum(), 1e-6)
            assertTrue(e.probs.values.all { it in 0.0..1.0 })
            assertEquals(ColdStartRules.VERSION, e.modelVersion)
        }
    }

    @Test fun baselineWindowIsCalm() {
        assertEquals(ContextState.CALM, model.estimate(win()).top)
    }

    @Test fun lateNightUseIsSleepless() {
        assertEquals(ContextState.SLEEPLESS, model.estimate(win(mapOf("night_screen_min" to 90.0))).top)
    }

    @Test fun highUnlockAndCheckingZIsWorry() {
        assertEquals(ContextState.WORRY, model.estimate(win(z = mapOf("unlock_count" to 4.0, "checking_sessions" to 4.0))).top)
    }

    @Test fun beforeBaselineZIsIgnored() {
        // AC-FR-09 / FR-11: null z-scores (baseline not ready) contribute nothing
        assertEquals(ContextState.CALM, model.estimate(win(z = mapOf("unlock_count" to null))).top)
    }

    @Test fun gapFlagDampsSignals() {
        val v = mapOf("night_screen_min" to 40.0)
        val normal = model.estimate(win(v)).probs.getValue(ContextState.SLEEPLESS)
        val gapped = model.estimate(win(v, gap = true)).probs.getValue(ContextState.SLEEPLESS)
        assertTrue(gapped < normal)
    }
}
