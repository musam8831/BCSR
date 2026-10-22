package org.pide.bcsr.core.recommender

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.util.Random

class RecommenderTest {
    private val now = LocalDateTime.of(2026, 11, 2, 19, 0)

    /** TC-FR-15-04 (AC-FR-15): over 10,000 random contexts, every recommendation is approved and mapped to the state. */
    @Test fun onlyApprovedMappedItemsAreReturned() {
        val rng = Random(15)
        val states = listOf("CS-01", "CS-02", "CS-04", "CS-06")
        repeat(10_000) {
            val items = List(rng.nextInt(8)) { i ->
                Candidate("DUA-%04d".format(i), states.shuffled(rng).take(1 + rng.nextInt(2)).toSet(), rng.nextBoolean())
            }
            val s = states[rng.nextInt(states.size)]
            val r = Recommender(rng).recommend(s, "x", now, items, emptyMap())
            if (r != null) {
                val c = items.first { it.id == r.itemId }
                assertTrue(c.approved && s in c.states)
            } else {
                assertTrue(items.none { it.approved && s in it.states })
            }
        }
    }

    @Test fun notShownAgainWithin24Hours() {
        val rec = Recommender(Random(1))
        val items = listOf(Candidate("A", setOf("CS-02"), true))
        assertNull(rec.recommend("CS-02", "x", now, items, mapOf("A" to now.minusHours(23))))
        assertEquals("A", rec.recommend("CS-02", "x", now, items, mapOf("A" to now.minusHours(24)))?.itemId)
    }

    /** AC-FR-16 / AC-FR-29: "not relevant" feedback lowers how often an item is chosen in that state. */
    @Test fun notRelevantFeedbackLowersSelection() {
        val rec = Recommender(Random(29))
        val items = listOf(Candidate("A", setOf("CS-02"), true), Candidate("B", setOf("CS-02"), true))
        repeat(20) { rec.feedback("CS-02", "A", Feedback.NOT_RELEVANT) }
        val picks = (1..1000).count { rec.recommend("CS-02", "x", now, items, emptyMap())?.itemId == "A" }
        assertTrue("A chosen $picks / 1000", picks < 150)
        rec.feedback("CS-02", "B", Feedback.NOT_NOW)
        assertEquals(1.0 to 1.0, rec.armState("CS-02", "B")) // not now = timing, no update
    }

    /** AC-NFR-08: the reason names the inputs, never a diagnosis. */
    @Test fun whyNamesStateAndTime() {
        val r = Recommender(Random(8)).recommend("CS-03", "restless", now, listOf(Candidate("A", setOf("CS-03"), true)), emptyMap())!!
        assertEquals("You said you feel restless, and it is evening.", r.why)
    }
}
