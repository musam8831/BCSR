package org.pide.bcsr.core.inference

import org.pide.bcsr.core.features.FeatureWindow
import kotlin.math.exp
import kotlin.math.max

/** Context states, taxonomy v1 (GOV-02 §1). [passive] = may be estimated from phone signals (FR-10). */
enum class ContextState(val id: String, val passive: Boolean) {
    CALM("CS-01", true), WORRY("CS-02", true), RESTLESS("CS-03", true), LOW("CS-04", true),
    ANGER("CS-05", false), SLEEPLESS("CS-06", true), LONELY("CS-07", false), FATIGUE("CS-08", true),
    GRATEFUL("CS-09", false), CRISIS("CS-10", false);

    companion object {
        val PASSIVE: List<ContextState> = entries.filter { it.passive }
        fun byId(id: String): ContextState = entries.first { it.id == id }
    }
}

/** Probabilities over the passive states only; never a diagnosis (AGENTS.md §3). */
data class StateEstimate(val probs: Map<ContextState, Double>, val modelVersion: String) {
    val top: ContextState get() = probs.maxBy { it.value }.key
}

fun interface StateEstimator {
    fun estimate(window: FeatureWindow): StateEstimate
}

/**
 * Cold-start rules model B1 (FR-11): used until a personal model exists. Scores come from FS-001 hypotheses
 * (GOV-02 §1, RES-02 evidence levels) and a softmax turns them into probabilities. Missing features score 0.
 * Model card: docs/04-ai-ml/model-cards/MC-coldstart-0.1.md.
 */
class ColdStartRules : StateEstimator {
    override fun estimate(window: FeatureWindow): StateEstimate {
        val v = window.values
        fun z(name: String) = max(0.0, window.z[name] ?: 0.0)
        val scores = mapOf(
            ContextState.CALM to 1.0,
            ContextState.WORRY to 0.5 * z("unlock_count") + 0.5 * z("checking_sessions"),
            ContextState.RESTLESS to ((v["longest_session_min"] ?: 0.0) - 45.0).coerceAtLeast(0.0) / 30.0,
            ContextState.LOW to 0.5 * z("mobility_still_ratio"),
            ContextState.SLEEPLESS to (v["night_screen_min"] ?: 0.0) / 20.0,
            ContextState.FATIGUE to ((v["longest_productivity_min"] ?: 0.0) - 90.0).coerceAtLeast(0.0) / 45.0,
        )
        // A gap-flagged window has unreliable features: damp every signal towards the calm baseline (TH-16).
        val damp = if (window.gapFlag) 0.5 else 1.0
        val logits = scores.mapValues { (s, x) -> if (s == ContextState.CALM) x else x * damp }
        val m = logits.values.max()
        val e = logits.mapValues { exp(it.value - m) }
        val sum = e.values.sum()
        return StateEstimate(e.mapValues { it.value / sum }, VERSION)
    }

    companion object {
        const val VERSION = "coldstart-0.1"
    }
}
