package io.github.reactivecircus.coroutines.test.ext

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThrows
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowRecorderTest {

    private val testScope = TestCoroutineScope()

    private val flowRecorder = FlowRecorder<Int>(testScope)

    @Test
    fun `can take first n recorded values from FlowRecorder when n is less or equal to the number of all recorded values`() {
        flowOf(1, 2, 3).recordWith(flowRecorder)

        assertThat(flowRecorder.take(2))
            .containsExactly(1, 2)

        assertThat(flowRecorder.take(1))
            .containsExactly(3)
    }

    @Test
    fun `throws exception when taking less than 1 recorded value from FlowRecorder`() {
        emptyFlow<Int>().recordWith(flowRecorder)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            flowRecorder.take(0)
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Least number of values to take is 1.")
    }

    @Test
    fun `throws exception when taking more than the total number of recorded value from FlowRecorder`() {
        flowOf(1).recordWith(flowRecorder)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            flowRecorder.take(2)
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Taking 2 but only 1 value(s) have been recorded.")
    }

    @Test
    fun `can take all recorded values from FlowRecorder`() = testScope.runBlockingTest {
        val flow = flow {
            delay(200)
            emit(1)
            emit(2)
            emit(3)
        }
        flow.recordWith(flowRecorder)

        assertThat(flowRecorder.takeAll())
            .isEmpty()

        delay(200)

        assertThat(flowRecorder.takeAll())
            .containsExactly(1, 2, 3)
    }

    @Test
    fun `recorded values are cleared after reset`() {
        flowOf(1, 2, 3).recordWith(flowRecorder)

        flowRecorder.reset()

        assertThat(flowRecorder.takeAll())
            .isEmpty()
    }
}
