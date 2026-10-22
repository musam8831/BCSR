package org.pide.bcsr.core.recommender

import java.time.Duration
import java.time.LocalDateTime
import java.util.Random
import kotlin.math.ln
import kotlin.math.pow

/** What the recommender needs from a catalogue item; scripture text never passes through here. */
data class Candidate(val id: String, val states: Set<String>, val approved: Boolean)

enum class Feedback { HELPFUL, NOT_NOW, NOT_RELEVANT }

data class Recommendation(val itemId: String, val why: String)

/**
 * Thompson sampling with one Beta(α, β) arm per (state, item) (FR-16).
 * Constraints before sampling: approved only (FR-15), mapped to the state by the scholar (CR-05), not shown in the
 * last [noRepeat] (FR-17). Feedback: helpful → success, not relevant → failure, not now → no update (it is about timing).
 */
class Recommender(private val rng: Random = Random(), private val noRepeat: Duration = Duration.ofHours(24)) {
    private val arms = HashMap<Pair<String, String>, DoubleArray>() // (stateId, itemId) → [α, β]

    fun recommend(
        stateId: String,
        stateWords: String,
        now: LocalDateTime,
        items: List<Candidate>,
        lastShown: Map<String, LocalDateTime>,
    ): Recommendation? {
        val eligible = items.filter { c ->
            c.approved && stateId in c.states && lastShown[c.id]?.let { Duration.between(it, now) >= noRepeat } != false
        }
        if (eligible.isEmpty()) return null
        val best = eligible.maxBy { c -> arm(stateId, c.id).let { sampleBeta(it[0], it[1]) } }
        return Recommendation(best.id, "You said you feel $stateWords, and it is ${partOfDay(now)}.")
    }

    fun feedback(stateId: String, itemId: String, f: Feedback) {
        val a = arm(stateId, itemId)
        when (f) {
            Feedback.HELPFUL -> a[0] += 1.0
            Feedback.NOT_RELEVANT -> a[1] += 1.0
            Feedback.NOT_NOW -> Unit
        }
    }

    fun armState(stateId: String, itemId: String): Pair<Double, Double> = arm(stateId, itemId).let { it[0] to it[1] }

    private fun arm(s: String, i: String) = arms.getOrPut(s to i) { doubleArrayOf(1.0, 1.0) }

    // Beta(α, β) via two Gamma draws (Marsaglia–Tsang); α, β ≥ 1 here, so no small-shape correction is needed.
    private fun sampleBeta(a: Double, b: Double): Double {
        val x = sampleGamma(a)
        return x / (x + sampleGamma(b))
    }

    private fun sampleGamma(k: Double): Double {
        val d = k - 1.0 / 3.0
        val c = 1.0 / kotlin.math.sqrt(9.0 * d)
        while (true) {
            val x = rng.nextGaussian()
            val v = (1.0 + c * x).pow(3)
            if (v <= 0) continue
            val u = rng.nextDouble()
            if (ln(u) < 0.5 * x * x + d - d * v + d * ln(v)) return d * v
        }
    }
}

fun partOfDay(t: LocalDateTime): String = when (t.hour) {
    in 4..11 -> "morning"
    in 12..16 -> "afternoon"
    in 17..20 -> "evening"
    else -> "night"
}
