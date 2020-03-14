package io.github.reactivecircus.livedata.test.ext

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

@ExperimentalStdlibApi
class TestObserverTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val liveData = MutableLiveData<Int>()

    private val testObserver = TestObserver<Int>()

    @Test
    fun `can take first n recorded values from TestObserver when n is less or equal to the number of all recorded values`() {
        liveData.observeForever(testObserver)

        liveData.value = 1
        liveData.value = 2
        liveData.value = 3

        assertThat(testObserver.take(2))
            .containsExactly(1, 2)

        assertThat(testObserver.take(1))
            .containsExactly(3)
    }

    @Test
    fun `throws exception when taking less than 1 recorded value from TestObserver`() {
        liveData.observeForever(testObserver)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            testObserver.take(0)
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Least number of values to take is 1.")
    }

    @Test
    fun `throws exception when taking more than the total number of recorded value from TestObserver`() {
        liveData.observeForever(testObserver)

        liveData.value = 1

        val exception = assertThrows(IllegalArgumentException::class.java) {
            testObserver.take(2)
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Taking 2 but only 1 value(s) have been recorded.")
    }

    @Test
    fun `can take all recorded values from TestObserver`() {
        liveData.observeForever(testObserver)

        assertThat(testObserver.takeAll())
            .isEmpty()

        liveData.value = 1
        liveData.value = 2
        liveData.value = 3

        assertThat(testObserver.takeAll())
            .containsExactly(1, 2, 3)
    }

    @Test
    fun `recorded values are cleared after reset`() {
        liveData.observeForever(testObserver)

        liveData.value = 1
        liveData.value = 2
        liveData.value = 3

        testObserver.reset()

        assertThat(testObserver.takeAll())
            .isEmpty()
    }
}
