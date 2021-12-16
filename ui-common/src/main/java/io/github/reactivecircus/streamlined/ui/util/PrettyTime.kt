@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Converts the timestamp to a formatted String
 */
fun Long.toFormattedDateString(
    pattern: String,
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale)
    return Instant.ofEpochMilli(this).atZone(zoneId).format(formatter)
        // make sure "." is removed when using three-letter abbreviation for month
        .replace(".", "")
}

/**
 * Returns prettified duration between a previous timestamp and now.
 */
fun Long.timeAgo(
    fallbackDatePattern: String,
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault(),
    clock: Clock = RealClock()
): String {
    val timeAgo = (clock.currentTimeMillis - this)
        .toDuration(DurationUnit.MILLISECONDS)
    return when {
        timeAgo < 1.minutes -> "Moments ago"
        timeAgo < 1.hours -> {
            if (timeAgo < 2.minutes) {
                "1 minute ago"
            } else {
                "${timeAgo.inWholeMinutes} minutes ago"
            }
        }
        timeAgo < 1.days -> {
            if (timeAgo < 2.hours) {
                "1 hour ago"
            } else {
                "${timeAgo.inWholeHours.toInt()} hours ago"
            }
        }
        timeAgo < 7.days -> {
            if (timeAgo < 2.days) {
                "Yesterday"
            } else {
                "${timeAgo.inWholeDays.toInt()} days ago"
            }
        }
        else -> this.toFormattedDateString(fallbackDatePattern, zoneId, locale)
    }
}

interface Clock {
    val currentTimeMillis: Long
}

internal class RealClock : Clock {
    override val currentTimeMillis: Long
        get() = System.currentTimeMillis()
}
