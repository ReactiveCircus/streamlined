package io.github.reactivecircus.streamlined.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Converts the timestamp to a formatted String
 */
fun Long.toFormattedDateString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(formatter)
}
