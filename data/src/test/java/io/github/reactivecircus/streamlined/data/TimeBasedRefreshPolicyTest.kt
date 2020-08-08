package io.github.reactivecircus.streamlined.data

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.store.ext.RefreshScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.time.TestTimeSource
import kotlin.time.seconds

@ExperimentalCoroutinesApi
class TimeBasedRefreshPolicyTest {

    @Test
    fun `non-positive expiration is not allowed`() {
        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            TimeBasedRefreshPolicy(
                expiration = 0.seconds
            )
        }

        assertThat(exception1)
            .hasMessageThat()
            .isEqualTo("Expiration for refresh policy must be positive.")

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            TimeBasedRefreshPolicy(
                expiration = (-1).seconds
            )
        }

        assertThat(exception2)
            .hasMessageThat()
            .isEqualTo("Expiration for refresh policy must be positive.")
    }

    @Test
    fun `shouldRefresh for a given scope returns true when no refresh has ever been recorded for the same scope`() =
        runBlockingTest {
            val scope1 = RefreshScope("scope1")
            val scope2 = RefreshScope("scope2")
            val refreshPolicy = TimeBasedRefreshPolicy()

            assertThat(refreshPolicy.shouldRefresh(scope1))
                .isTrue()

            // record refresh with a different scope
            refreshPolicy.onRefreshed(scope2)

            // should still return true for original scope
            assertThat(refreshPolicy.shouldRefresh(scope1))
                .isTrue()
        }

    @Test
    fun `shouldRefresh for a given scope returns false until expiration after recording a refresh`() =
        runBlockingTest {
            val scope = RefreshScope("scope")
            val testTimeSource = TestTimeSource()
            val refreshPolicy = TimeBasedRefreshPolicy(
                expiration = 10.seconds,
                timeSource = testTimeSource
            )
            refreshPolicy.onRefreshed(scope)

            assertThat(refreshPolicy.shouldRefresh(scope))
                .isFalse()

            testTimeSource += 9.seconds

            assertThat(refreshPolicy.shouldRefresh(scope))
                .isFalse()

            testTimeSource += 1.seconds

            assertThat(refreshPolicy.shouldRefresh(scope))
                .isTrue()
        }

    @Test
    fun `expiration time is extended after recording a new refresh`() = runBlockingTest {
        val scope = RefreshScope("scope")
        val testTimeSource = TestTimeSource()
        val refreshPolicy = TimeBasedRefreshPolicy(
            expiration = 10.seconds,
            timeSource = testTimeSource
        )
        refreshPolicy.onRefreshed(scope)

        testTimeSource += 9.seconds

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        // expiry is updated to 10 seconds from now
        refreshPolicy.onRefreshed(scope)

        testTimeSource += 1.seconds

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        testTimeSource += 8.seconds

        // expiry is updated again to 10 seconds from now
        refreshPolicy.onRefreshed(scope)

        testTimeSource += 1.seconds

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        testTimeSource += 8.seconds

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        testTimeSource += 1.seconds

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isTrue()
    }

    @Test
    fun `expiration times are independent for different refresh scopes`() = runBlockingTest {
        val scope1 = RefreshScope("scope1")
        val scope2 = RefreshScope("scope2")
        val testTimeSource = TestTimeSource()
        val refreshPolicy = TimeBasedRefreshPolicy(
            expiration = 10.seconds,
            timeSource = testTimeSource
        )
        refreshPolicy.onRefreshed(scope1)

        testTimeSource += 5.seconds

        refreshPolicy.onRefreshed(scope2)

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isFalse()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isFalse()

        testTimeSource += 5.seconds

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isFalse()

        testTimeSource += 5.seconds

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isTrue()
    }

    @Test
    fun `refresh logs for all scopes are cleared after reset`() = runBlockingTest {
        val scope1 = RefreshScope("scope1")
        val scope2 = RefreshScope("scope2")
        val testTimeSource = TestTimeSource()
        val refreshPolicy = TimeBasedRefreshPolicy(
            expiration = 10.seconds,
            timeSource = testTimeSource
        )
        refreshPolicy.onRefreshed(scope1)

        testTimeSource += 5.seconds

        refreshPolicy.onRefreshed(scope2)

        testTimeSource += 4.seconds

        refreshPolicy.reset()

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isTrue()

        testTimeSource += 11.seconds

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isTrue()
    }
}
