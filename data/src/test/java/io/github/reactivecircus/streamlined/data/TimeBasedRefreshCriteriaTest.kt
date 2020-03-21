package io.github.reactivecircus.streamlined.data

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.store.ext.RefreshScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.TestTimeSource
import kotlin.time.seconds

@ExperimentalTime
@ExperimentalCoroutinesApi
class TimeBasedRefreshCriteriaTest {

    @Test
    fun `non-positive expiration is not allowed`() {
        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            TimeBasedRefreshCriteria(
                expiration = 0.seconds
            )
        }

        assertThat(exception1)
            .hasMessageThat()
            .isEqualTo("Expiration for refresh criteria must be positive.")

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            TimeBasedRefreshCriteria(
                expiration = (-1).seconds
            )
        }

        assertThat(exception2)
            .hasMessageThat()
            .isEqualTo("Expiration for refresh criteria must be positive.")
    }

    @Test
    fun `shouldRefresh for a given scope returns true when no refresh has ever been recorded for the same scope`() =
        runBlockingTest {
            val scope1 = RefreshScope("scope1")
            val scope2 = RefreshScope("scope2")
            val refreshCriteria = TimeBasedRefreshCriteria()

            assertThat(refreshCriteria.shouldRefresh(scope1))
                .isTrue()

            // record refresh with a different scope
            refreshCriteria.onRefreshed(scope2)

            // should still return true for original scope
            assertThat(refreshCriteria.shouldRefresh(scope1))
                .isTrue()
        }

    @Test
    fun `shouldRefresh for a given scope returns false until expiration after recording a refresh`() =
        runBlockingTest {
            val scope = RefreshScope("scope")
            val testTimeSource = TestTimeSource()
            val refreshCriteria = TimeBasedRefreshCriteria(
                expiration = 10.seconds,
                timeSource = testTimeSource
            )
            refreshCriteria.onRefreshed(scope)

            assertThat(refreshCriteria.shouldRefresh(scope))
                .isFalse()

            testTimeSource += 9.seconds

            assertThat(refreshCriteria.shouldRefresh(scope))
                .isFalse()

            testTimeSource += 1.seconds

            assertThat(refreshCriteria.shouldRefresh(scope))
                .isTrue()
        }

    @Test
    fun `expiration time is extended after recording a new refresh`() = runBlockingTest {
        val scope = RefreshScope("scope")
        val testTimeSource = TestTimeSource()
        val refreshCriteria = TimeBasedRefreshCriteria(
            expiration = 10.seconds,
            timeSource = testTimeSource
        )
        refreshCriteria.onRefreshed(scope)

        testTimeSource += 9.seconds

        assertThat(refreshCriteria.shouldRefresh(scope))
            .isFalse()

        // expiry is updated to 10 seconds from now
        refreshCriteria.onRefreshed(scope)

        testTimeSource += 1.seconds

        assertThat(refreshCriteria.shouldRefresh(scope))
            .isFalse()

        testTimeSource += 8.seconds

        // expiry is updated again to 10 seconds from now
        refreshCriteria.onRefreshed(scope)

        testTimeSource += 1.seconds

        assertThat(refreshCriteria.shouldRefresh(scope))
            .isFalse()

        testTimeSource += 8.seconds

        assertThat(refreshCriteria.shouldRefresh(scope))
            .isFalse()

        testTimeSource += 1.seconds

        assertThat(refreshCriteria.shouldRefresh(scope))
            .isTrue()
    }

    @Test
    fun `expiration times are independent for different refresh scopes`() = runBlockingTest {
        val scope1 = RefreshScope("scope1")
        val scope2 = RefreshScope("scope2")
        val testTimeSource = TestTimeSource()
        val refreshCriteria = TimeBasedRefreshCriteria(
            expiration = 10.seconds,
            timeSource = testTimeSource
        )
        refreshCriteria.onRefreshed(scope1)

        testTimeSource += 5.seconds

        refreshCriteria.onRefreshed(scope2)

        assertThat(refreshCriteria.shouldRefresh(scope1))
            .isFalse()

        assertThat(refreshCriteria.shouldRefresh(scope2))
            .isFalse()

        testTimeSource += 5.seconds

        assertThat(refreshCriteria.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshCriteria.shouldRefresh(scope2))
            .isFalse()

        testTimeSource += 5.seconds

        assertThat(refreshCriteria.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshCriteria.shouldRefresh(scope2))
            .isTrue()
    }

    @Test
    fun `refresh logs for all scopes are cleared after reset`() = runBlockingTest {
        val scope1 = RefreshScope("scope1")
        val scope2 = RefreshScope("scope2")
        val testTimeSource = TestTimeSource()
        val refreshCriteria = TimeBasedRefreshCriteria(
            expiration = 10.seconds,
            timeSource = testTimeSource
        )
        refreshCriteria.onRefreshed(scope1)

        testTimeSource += 5.seconds

        refreshCriteria.onRefreshed(scope2)

        testTimeSource += 4.seconds

        refreshCriteria.reset()

        assertThat(refreshCriteria.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshCriteria.shouldRefresh(scope2))
            .isTrue()

        testTimeSource += 11.seconds

        assertThat(refreshCriteria.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshCriteria.shouldRefresh(scope2))
            .isTrue()
    }
}
