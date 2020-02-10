package io.github.reactivecircus.streamlined.remote.helper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Convert a date time [String] from a News API response to a standard UTC timestamp in [Long].
 *
 * E.g. "2020-02-07T22:37:23Z" -> 1581287843000
 */
fun String.toTimestamp(): Long {
    val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return dateFormat.parse(replace("[TZ]".toRegex(), " ")).time
}

private const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
