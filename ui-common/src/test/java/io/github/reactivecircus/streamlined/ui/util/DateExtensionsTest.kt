package io.github.reactivecircus.streamlined.ui.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

class DateExtensionsTest {

    @Test
    fun `timestamp can be converted to a formatted date String given a date pattern`() {
        val pattern = "EEE d MMM 'at' h:mm a"

        // Saturday, 24 August 2019 20:00:00
        val timestamp = LocalDateTime.of(
            2019, Month.AUGUST, 24, 20, 0
        ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        assertThat(timestamp.toFormattedDateString(pattern))
            .isEqualTo("Sat 24 Aug at 8:00 PM")
    }
}
