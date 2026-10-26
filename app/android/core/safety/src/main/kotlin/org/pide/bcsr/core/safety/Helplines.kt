package org.pide.bcsr.core.safety

import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class Verification { VERIFIED, PARTLY_VERIFIED, NOT_VERIFIED }
enum class Purpose { EMERGENCY, MENTAL_HEALTH }

data class Helpline(
    val name: String,
    val number: String,
    val purpose: Purpose,
    val status: Verification,
    val checkedOn: LocalDate,
)

/** SAF-02 Pakistan entries, copied from docs/06-security-privacy-safety/safety/helpline-directory.md (checked 2026-10-21). */
val PAKISTAN: List<Helpline> = listOf(
    Helpline("Rescue 1122", "1122", Purpose.EMERGENCY, Verification.VERIFIED, LocalDate.of(2026, 10, 21)),
    Helpline("National Mental Health Helpline", "1166", Purpose.MENTAL_HEALTH, Verification.PARTLY_VERIFIED, LocalDate.of(2026, 10, 21)),
    Helpline("Umang Pakistan", "0311-7786264", Purpose.MENTAL_HEALTH, Verification.NOT_VERIFIED, LocalDate.of(2026, 10, 21)),
)

const val MAX_AGE_DAYS = 90L // SR-05 / AC-SR-05: reviewed at least every 3 months

// a check date in the future is a data error, not "fresh" (AG-10 R-12)
private fun fresh(h: Helpline, today: LocalDate) = !h.checkedOn.isAfter(today) && ChronoUnit.DAYS.between(h.checkedOn, today) <= MAX_AGE_DAYS

/**
 * Helplines the safety screen shows (AC-FR-24, AC-SR-01). Release builds: only fully verified, fresh entries.
 * Development builds may also show partly verified entries (SAF-02 "Before release"). Never a not-verified entry.
 */
fun shownHelplines(today: LocalDate, devBuild: Boolean, directory: List<Helpline> = PAKISTAN): List<Helpline> {
    val shown = directory.filter { fresh(it, today) && (it.status == Verification.VERIFIED || (devBuild && it.status == Verification.PARTLY_VERIFIED)) }
    if (shown.any { it.purpose == Purpose.EMERGENCY }) return shown
    // SAF-01 §3: the emergency action is always shown. Past its review date, the last verified emergency number stays
    // (release is still blocked by releaseBlockers); only lines never fully verified drop out.
    val floor = directory.filter { it.purpose == Purpose.EMERGENCY && it.status == Verification.VERIFIED }.maxByOrNull { it.checkedOn }
    return listOfNotNull(floor) + shown
}

/** Reasons a release must be blocked (SAF-02, SK-10 step 4); empty = the safety pathway may ship. */
fun releaseBlockers(today: LocalDate, directory: List<Helpline> = PAKISTAN): List<String> = buildList {
    val ok = directory.filter { it.status == Verification.VERIFIED && fresh(it, today) }
    if (ok.none { it.purpose == Purpose.EMERGENCY }) add("no verified, current emergency number")
    if (ok.none { it.purpose == Purpose.MENTAL_HEALTH }) add("no verified, current mental-health helpline")
    directory.filter { it.status != Verification.NOT_VERIFIED && !fresh(it, today) }.forEach { add("${it.name} not reviewed in $MAX_AGE_DAYS days") }
}
