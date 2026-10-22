package org.pide.bcsr.core.policy

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * City the user picks in settings. Salah times need coordinates, but BCSR never reads or stores the user's
 * location (PR-04): the user chooses a city, and only the city's public centre point is used.
 */
enum class City(val label: String, val lat: Double, val lon: Double) {
    LAHORE("Lahore", 31.5204, 74.3587),
    ISLAMABAD("Islamabad", 33.6844, 73.0479),
    KARACHI("Karachi", 24.8607, 67.0011),
    PESHAWAR("Peshawar", 34.0151, 71.5249),
    QUETTA("Quetta", 30.1798, 66.9750),
    FAISALABAD("Faisalabad", 31.4504, 73.1350),
    MULTAN("Multan", 30.1575, 71.5249),
}

enum class AsrMethod { STANDARD, HANAFI }

val PAKISTAN_ZONE: ZoneId = ZoneId.of("Asia/Karachi")

/**
 * Today's five Salah times in local time for [policy decide()][decide] (FR-21, T-M5-008).
 * Defaults: University of Islamic Sciences, Karachi method and standard Asr (the user can change both in settings).
 */
fun salahTimes(
    date: LocalDate,
    city: City,
    method: CalculationMethod = CalculationMethod.KARACHI,
    asr: AsrMethod = AsrMethod.STANDARD,
    zone: ZoneId = PAKISTAN_ZONE,
): List<LocalTime> {
    val params = method.parameters.apply { madhab = if (asr == AsrMethod.HANAFI) Madhab.HANAFI else Madhab.SHAFI }
    val t = PrayerTimes(Coordinates(city.lat, city.lon), DateComponents(date.year, date.monthValue, date.dayOfMonth), params)
    return listOf(t.fajr, t.dhuhr, t.asr, t.maghrib, t.isha).map {
        // Adhan keeps the current clock's milliseconds in its results; prayer times are minute-precise.
        Instant.ofEpochMilli(it.time).atZone(zone).toLocalTime().truncatedTo(ChronoUnit.MINUTES)
    }
}
