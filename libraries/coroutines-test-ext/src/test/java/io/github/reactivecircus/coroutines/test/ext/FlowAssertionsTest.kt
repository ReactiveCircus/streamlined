package io.github.reactivecircus.coroutines.test.ext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class FlowAssertionsTest {

    private val testScope = TestCoroutineScope()

    @Test
    fun `emitsExactly succeeds when only the expected items have been emitted by the Flow`() =
        testScope.runBlockingTest {
            val flow = flow {
                emit(1)
                delay(100)
                emit(2)
                delay(100)
                emit(3)
            }

            assertThat(flow).emitsExactly(1, 2, 3)
        }

    @Test
    fun `emitsExactly fails when unexpected items have been emitted by the Flow`() =
        testScope.runBlockingTest {
            val flow = flow {
                emit(1)
                delay(100)
                emit(20)
                delay(100)
                emit(3)
            }

            assertThrows<AssertionError> {
                assertThat(flow).emitsExactly(1, 2, 3)
            }
        }

    @Test
    fun `emitsExactly fails when subset of the expected items have been emitted by the Flow`() =
        testScope.runBlockingTest {
            val flow = flow {
                emit(1)
                delay(100)
                emit(2)
            }

            assertThrows<AssertionError> {
                testScope.assertThat(flow).emitsExactly(1, 2, 3)
            }
        }

    @Test
    fun `emitsExactly fails when additional items have been emitted by the Flow after emitting the expected ones`() =
        testScope.runBlockingTest {
            val flow = flow {
                emit(1)
                delay(100)
                emit(2)
                delay(100)
                emit(3)
                delay(100)
                emit(4)
            }

            assertThrows<AssertionError> {
                assertThat(flow).emitsExactly(1, 2, 3)
            }
        }
}
