package io.github.reactivecircus.streamlined.data.mapper

import java.time.Instant

/**
 * Converts a [String] in ISO-8601 format to a UTC timestamp in [Long].
 *
 * E.g. "2020-02-07T22:37:23Z" -> 1581115043000
 */
fun String.toTimestamp(): Long {
    return Instant.parse(this).toEpochMilli()
}
