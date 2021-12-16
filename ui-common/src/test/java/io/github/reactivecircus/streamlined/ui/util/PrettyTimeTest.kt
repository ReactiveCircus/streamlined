package io.github.reactivecircus.streamlined.ui.util

import com.google.common.truth.Truth.assertThat
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import org.junit.Test

class PrettyTimeTest {

    private val pattern = "EEE d MMM 'at' h:mm a"
    private val zoneId = ZoneId.of("GMT")
    private val locale = Locale.ENGLISH

    @Test
    fun `timestamp can be converted to a formatted date string given a date pattern`() {
        val timestamp = LocalDateTime.of(
            2019,
            Month.AUGUST,
            24,
            20,
            0
        ).atZone(zoneId).toInstant().toEpochMilli()

        assertThat(timestamp.toFormattedDateString(pattern, zoneId, locale))
            .isEqualTo("Sat 24 Aug at 8:00 PM")
    }

    @Test
    fun `0 or negative timestamp can be converted to a formatted date string given a date pattern`() {
        val timestamp1 = LocalDateTime.of(
            1970,
            Month.JANUARY,
            1,
            0,
            0
        ).atZone(zoneId).toInstant().toEpochMilli()

        assertThat(timestamp1.toFormattedDateString(pattern, zoneId, locale))
            .isEqualTo("Thu 1 Jan at 12:00 AM")

        val timestamp2 = LocalDateTime.of(
            1969,
            Month.DECEMBER,
            31,
            23,
            59
        ).atZone(zoneId).toInstant().toEpochMilli()

        assertThat(timestamp2.toFormattedDateString(pattern, zoneId, locale))
            .isEqualTo("Wed 31 Dec at 11:59 PM")
    }

    @Test
    fun `timeAgo() returns 'Moments ago' when given time from now is less than 1 minute`() {
        val nowMillis = 61.seconds.inWholeMilliseconds
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            1.seconds.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 minute ago")
        assertThat(
            30.seconds.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Moments ago")
        assertThat(
            61.seconds.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Moments ago")
        assertThat(
            62.seconds.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Moments ago")
    }

    @Test
    fun `timeAgo() returns 'x minute(s) ago' when given time from now is between 1 hour and 1 minute`() {
        val nowMillis = 61.minutes.inWholeMilliseconds
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            1.minutes.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 hour ago")
        assertThat(
            30.minutes.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("31 minutes ago")
        assertThat(
            60.minutes.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 minute ago")
    }

    @Test
    fun `timeAgo() returns 'x hour(s) ago' when given time from now is between 1 day and 1 hour`() {
        val nowMillis = 25.hours.inWholeMilliseconds
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            1.hours.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Yesterday")
        assertThat(
            12.hours.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("13 hours ago")
        assertThat(
            24.hours.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 hour ago")
    }

    @Test
    fun `timeAgo() returns 'Yesterday' or 'x days ago' when given time from now is between 1 week and 1 day`() {
        val nowMillis = 8.days.inWholeMilliseconds
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            1.days.inWholeMilliseconds
                .timeAgo(pattern, zoneId, locale, fixedClock)
        )
            .isEqualTo("Fri 2 Jan at 12:00 AM")
        assertThat(
            3.days.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("5 days ago")
        assertThat(
            7.days.inWholeMilliseconds.timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Yesterday")
    }

    @Test
    fun `timeAgo() returns formatted date string when given time from now is between at least 1 week`() {
        val nowMillis = 10.days.inWholeMilliseconds
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            3.days.inWholeMilliseconds
                .timeAgo(pattern, zoneId, locale, fixedClock)
        )
            .isEqualTo("Sun 4 Jan at 12:00 AM")
        assertThat(
            2.days.inWholeMilliseconds
                .timeAgo(pattern, zoneId, locale, fixedClock)
        )
            .isEqualTo("Sat 3 Jan at 12:00 AM")
    }

    @Test
    fun `timeAgo() returns formatted date string when given time from now is 0 or negative`() {
        val nowMillis = 18.days.inWholeMilliseconds
        val fixedClock = FixedClock(nowMillis)

        assertThat(0L.timeAgo(pattern, zoneId, locale, fixedClock))
            .isEqualTo("Thu 1 Jan at 12:00 AM")
        assertThat((-1L).timeAgo(pattern, zoneId, locale, fixedClock))
            .isEqualTo("Wed 31 Dec at 11:59 PM")
    }
}

private class FixedClock(override val currentTimeMillis: Long) : Clock
