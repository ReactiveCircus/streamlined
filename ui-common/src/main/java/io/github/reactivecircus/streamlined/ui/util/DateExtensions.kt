package io.github.reactivecircus.streamlined.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Converts the timestamp to a formatted String
 */
fun Long.toFormattedDateString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(Locale.ENGLISH)
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(formatter)
}
