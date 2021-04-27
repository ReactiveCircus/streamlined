package io.github.reactivecircus.streamlined.ui.util

import com.google.common.truth.Truth.assertThat
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.util.Locale
import kotlin.time.Duration
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
        val nowMillis = Duration.seconds(61).inWholeMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            Duration.seconds(1).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 minute ago")
        assertThat(
            Duration.seconds(30).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Moments ago")
        assertThat(
            Duration.seconds(61).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Moments ago")
        assertThat(
            Duration.seconds(62).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Moments ago")
    }

    @Test
    fun `timeAgo() returns 'x minute(s) ago' when given time from now is between 1 hour and 1 minute`() {
        val nowMillis = Duration.minutes(61).inWholeMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            Duration.minutes(1).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 hour ago")
        assertThat(
            Duration.minutes(30).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("31 minutes ago")
        assertThat(
            Duration.minutes(60).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 minute ago")
    }

    @Test
    fun `timeAgo() returns 'x hour(s) ago' when given time from now is between 1 day and 1 hour`() {
        val nowMillis = Duration.hours(25).inWholeMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            Duration.hours(1).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Yesterday")
        assertThat(
            Duration.hours(12).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("13 hours ago")
        assertThat(
            Duration.hours(24).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("1 hour ago")
    }

    @Test
    fun `timeAgo() returns 'Yesterday' or 'x days ago' when given time from now is between 1 week and 1 day`() {
        val nowMillis = Duration.days(8).inWholeMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            Duration.days(1).inWholeMilliseconds.toLong()
                .timeAgo(pattern, zoneId, locale, fixedClock)
        )
            .isEqualTo("Fri 2 Jan at 12:00 AM")
        assertThat(
            Duration.days(3).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("5 days ago")
        assertThat(
            Duration.days(7).inWholeMilliseconds.toLong().timeAgo(pattern, clock = fixedClock)
        )
            .isEqualTo("Yesterday")
    }

    @Test
    fun `timeAgo() returns formatted date string when given time from now is between at least 1 week`() {
        val nowMillis = Duration.days(10).inWholeMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(
            Duration.days(3).inWholeMilliseconds.toLong()
                .timeAgo(pattern, zoneId, locale, fixedClock)
        )
            .isEqualTo("Sun 4 Jan at 12:00 AM")
        assertThat(
            Duration.days(2).inWholeMilliseconds.toLong()
                .timeAgo(pattern, zoneId, locale, fixedClock)
        )
            .isEqualTo("Sat 3 Jan at 12:00 AM")
    }

    @Test
    fun `timeAgo() returns formatted date string when given time from now is 0 or negative`() {
        val nowMillis = Duration.days(10).inWholeMilliseconds.toLong()
        val fixedClock = FixedClock(nowMillis)

        assertThat(0L.timeAgo(pattern, zoneId, locale, fixedClock))
            .isEqualTo("Thu 1 Jan at 12:00 AM")
        assertThat((-1L).timeAgo(pattern, zoneId, locale, fixedClock))
            .isEqualTo("Wed 31 Dec at 11:59 PM")
    }
}

private class FixedClock(override val currentTimeMillis: Long) : Clock
