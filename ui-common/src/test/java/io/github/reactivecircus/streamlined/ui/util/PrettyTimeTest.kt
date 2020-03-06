package io.github.reactivecircus.streamlined.ui.util

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
class PrettyTimeTest {

    private val pattern = "EEE d MMM 'at' h:mm a"

    @Test
    fun `timestamp can be converted to a formatted date string given a date pattern`() {
        val timestamp = LocalDateTime.of(
            2019, Month.AUGUST, 24, 20, 0
        ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        assertThat(timestamp.toFormattedDateString(pattern, Locale.ENGLISH))
            .isEqualTo("Sat 24 Aug at 8:00 PM")
    }

    @Test
    fun `throws exception when converting 0 or negative timestamp to formatted date string`() {
        assertThrows(IllegalArgumentException::class.java) {
            0L.toFormattedDateString(pattern)
        }

        assertThrows(IllegalArgumentException::class.java) {
            (-1L).toFormattedDateString(pattern)
        }
    }

    @Test
    fun `timeAgo() returns "Moments ago" when given time from now is less than 1 minute`() {
        val nowMillis = 61.seconds.inMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(1.seconds.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("1 minute ago")
        assertThat(30.seconds.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("Moments ago")
        assertThat(61.seconds.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("Moments ago")
        assertThat(62.seconds.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("Moments ago")
    }

    @Test
    fun `timeAgo() returns "x minute(s) ago" when given time from now is between 1 hour and 1 minute`() {
        val nowMillis = 61.minutes.inMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(1.minutes.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("1 hour ago")
        assertThat(30.minutes.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("31 minutes ago")
        assertThat(60.minutes.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("1 minute ago")
    }

    @Test
    fun `timeAgo() returns "x hour(s) ago" when given time from now is between 1 day and 1 hour`() {
        val nowMillis = 25.hours.inMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(1.hours.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("Yesterday")
        assertThat(12.hours.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("13 hours ago")
        assertThat(24.hours.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("1 hour ago")
    }

    @Test
    fun `timeAgo() returns "Yesterday" or "x days ago" when given time from now is between 1 week and 1 day`() {
        val nowMillis = 8.days.inMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(1.days.inMilliseconds.toLong().timeAgo(pattern, Locale.ENGLISH, fixedClock))
            .isEqualTo("Fri 2 Jan at 10:00 AM")
        assertThat(3.days.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("5 days ago")
        assertThat(7.days.inMilliseconds.toLong().timeAgo(pattern, clock = fixedClock))
            .isEqualTo("Yesterday")
    }

    @Test
    fun `timeAgo() returns formatted date string when given time from now is between at least 1 week`() {
        val nowMillis = 10.days.inMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(3.days.inMilliseconds.toLong().timeAgo(pattern, Locale.ENGLISH, fixedClock))
            .isEqualTo("Sun 4 Jan at 10:00 AM")
        assertThat(2.days.inMilliseconds.toLong().timeAgo(pattern, Locale.ENGLISH, fixedClock))
            .isEqualTo("Sat 3 Jan at 10:00 AM")
    }

    @Test
    fun `throws exception when converting 0 or negative timestamp to prettified time-ago string`() {
        assertThrows(IllegalArgumentException::class.java) {
            0L.timeAgo(pattern)
        }

        assertThrows(IllegalArgumentException::class.java) {
            (-1L).timeAgo(pattern)
        }
    }
}

private class FixedClock(override val currentTimeMillis: Long) : Clock
