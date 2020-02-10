package io.github.reactivecircus.streamlined.remote.helper

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.text.ParseException

class NewsApiDateParserTest {

    @Test
    fun `converts valid date time string to UTC timestamp`() {
        assertThat("2020-02-07T22:37:23Z".toTimestamp())
            .isEqualTo(1_581_115_043_000)
        assertThat("2020-02-13T10:00:03Z".toTimestamp())
            .isEqualTo(1_581_588_003_000)
    }

    @Test
    fun `throws exception when converting an invalid date time string to UTC timestamp`() {
        assertThrows(ParseException::class.java) {
            "2020/02/07 22:37:23".toTimestamp()
        }
    }
}
