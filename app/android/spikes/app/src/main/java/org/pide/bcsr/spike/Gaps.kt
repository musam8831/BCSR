package org.pide.bcsr.spike

/** Result of comparing heartbeat times with the scheduled interval (SPK-04). */
data class GapStats(val heartbeats: Int, val intervals: Int, val gaps: Int, val maxGapMs: Long) {
    val gapRate: Double get() = if (intervals == 0) 0.0 else gaps.toDouble() / intervals
}

/**
 * Counts intervals between consecutive heartbeats that exceed [toleranceFactor] × [expectedMs].
 * WorkManager may run a periodic job late within its flex window, so only clearly missed runs count as gaps.
 */
fun heartbeatGaps(timesMs: List<Long>, expectedMs: Long, toleranceFactor: Double = 2.0): GapStats {
    require(expectedMs > 0) { "expectedMs must be positive" }
    val sorted = timesMs.sorted()
    val deltas = sorted.zipWithNext { a, b -> b - a }
    val limit = (expectedMs * toleranceFactor).toLong()
    return GapStats(
        heartbeats = sorted.size,
        intervals = deltas.size,
        gaps = deltas.count { it > limit },
        maxGapMs = deltas.maxOrNull() ?: 0L,
    )
}
