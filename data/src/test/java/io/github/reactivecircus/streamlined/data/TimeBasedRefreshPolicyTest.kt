package io.github.reactivecircus.streamlined.data

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.store.ext.RefreshScope
import kotlin.time.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.time.TestTimeSource

@ExperimentalCoroutinesApi
class TimeBasedRefreshPolicyTest {

    @Test
    fun `non-positive expiration is not allowed`() {
        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            TimeBasedRefreshPolicy(
                expiration = Duration.seconds(0)
            )
        }

        assertThat(exception1)
            .hasMessageThat()
            .isEqualTo("Expiration for refresh policy must be positive.")

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            TimeBasedRefreshPolicy(
                expiration = Duration.seconds((-1))
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
                expiration = Duration.seconds(10),
                timeSource = testTimeSource
            )
            refreshPolicy.onRefreshed(scope)

            assertThat(refreshPolicy.shouldRefresh(scope))
                .isFalse()

            testTimeSource += Duration.seconds(9)

            assertThat(refreshPolicy.shouldRefresh(scope))
                .isFalse()

            testTimeSource += Duration.seconds(1)

            assertThat(refreshPolicy.shouldRefresh(scope))
                .isTrue()
        }

    @Test
    fun `expiration time is extended after recording a new refresh`() = runBlockingTest {
        val scope = RefreshScope("scope")
        val testTimeSource = TestTimeSource()
        val refreshPolicy = TimeBasedRefreshPolicy(
            expiration = Duration.seconds(10),
            timeSource = testTimeSource
        )
        refreshPolicy.onRefreshed(scope)

        testTimeSource += Duration.seconds(9)

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        // expiry is updated to 10 seconds from now
        refreshPolicy.onRefreshed(scope)

        testTimeSource += Duration.seconds(1)

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        testTimeSource += Duration.seconds(8)

        // expiry is updated again to 10 seconds from now
        refreshPolicy.onRefreshed(scope)

        testTimeSource += Duration.seconds(1)

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        testTimeSource += Duration.seconds(8)

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isFalse()

        testTimeSource += Duration.seconds(1)

        assertThat(refreshPolicy.shouldRefresh(scope))
            .isTrue()
    }

    @Test
    fun `expiration times are independent for different refresh scopes`() = runBlockingTest {
        val scope1 = RefreshScope("scope1")
        val scope2 = RefreshScope("scope2")
        val testTimeSource = TestTimeSource()
        val refreshPolicy = TimeBasedRefreshPolicy(
            expiration = Duration.seconds(10),
            timeSource = testTimeSource
        )
        refreshPolicy.onRefreshed(scope1)

        testTimeSource += Duration.seconds(5)

        refreshPolicy.onRefreshed(scope2)

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isFalse()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isFalse()

        testTimeSource += Duration.seconds(5)

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isFalse()

        testTimeSource += Duration.seconds(5)

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
            expiration = Duration.seconds(10),
            timeSource = testTimeSource
        )
        refreshPolicy.onRefreshed(scope1)

        testTimeSource += Duration.seconds(5)

        refreshPolicy.onRefreshed(scope2)

        testTimeSource += Duration.seconds(4)

        refreshPolicy.reset()

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isTrue()

        testTimeSource += Duration.seconds(11)

        assertThat(refreshPolicy.shouldRefresh(scope1))
            .isTrue()

        assertThat(refreshPolicy.shouldRefresh(scope2))
            .isTrue()
    }
}
