@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.ui.util

import android.annotation.SuppressLint
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.toDuration

/**
 * Converts the timestamp to a formatted String
 */
@SuppressLint("NewApi")
fun Long.toFormattedDateString(pattern: String, locale: Locale = Locale.getDefault()): String {
    require(this > 0) { "Timestamp must be positive." }
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale)
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(formatter)
        // make sure "." is removed when using three-letter abbreviation for month
        .replace(".", "")
}

/**
 * Returns prettified duration between a previous timestamp and now.
 */
@SuppressLint("NewApi")
@OptIn(ExperimentalTime::class)
fun Long.timeAgo(
    fallbackDatePattern: String,
    locale: Locale = Locale.getDefault(),
    clock: Clock = Clock.systemUTC()
): String {
    require(this > 0) { "Timestamp must be positive." }
    val timeAgo = (Instant.now(clock).toEpochMilli() - this)
        .toDuration(DurationUnit.MILLISECONDS)
    return when {
        timeAgo < 1.minutes -> "Moments ago"
        timeAgo < 1.hours -> {
            if (timeAgo < 2.minutes) {
                "1 minute ago"
            } else {
                "${timeAgo.inMinutes.toInt()} minutes ago"
            }
        }
        timeAgo < 1.days -> {
            if (timeAgo < 2.hours) {
                "1 hour ago"
            } else {
                "${timeAgo.inHours.toInt()} hours ago"
            }
        }
        timeAgo < 7.days -> {
            if (timeAgo < 2.days) {
                "Yesterday"
            } else {
                "${timeAgo.inDays.toInt()} days ago"
            }
        }
        else -> this.toFormattedDateString(fallbackDatePattern, locale)
    }
}