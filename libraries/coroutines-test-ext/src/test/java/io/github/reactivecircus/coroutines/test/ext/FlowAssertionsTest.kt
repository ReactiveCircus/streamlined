package io.github.reactivecircus.coroutines.test.ext

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
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
    fun `emitsExactly succeeds when the Flow emits exactly the expected item but is not yet complete`() =
        testScope.runBlockingTest {
            val flow = flow {
                emitAll(flowOf(1, 2, 3))
                suspendCancellableCoroutine<Unit> {}
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

            val exception = assertThrows<AssertionError> {
                assertThat(flow).emitsExactly(1, 2, 3)
            }
            assertThat(exception).hasMessageThat().contains(
                """
                    Flow did not emit exactly expected items
                    missing (1)   : 2
                    unexpected (1): 20
                    ---
                    expected      : [1, 2, 3]
                    but was       : [1, 20, 3]
                """.trimIndent()
            )
        }

    @Test
    fun `emitsExactly fails when subset of the expected items have been emitted by the Flow`() =
        testScope.runBlockingTest {
            val flow = flow {
                emit(1)
                delay(100)
                emit(2)
            }

            val exception = assertThrows<AssertionError> {
                testScope.assertThat(flow).emitsExactly(1, 2, 3)
            }
            assertThat(exception).hasMessageThat().contains(
                """
                    Flow did not emit exactly expected items
                    missing (1): 3
                    ---
                    expected   : [1, 2, 3]
                    but was    : [1, 2]
                """.trimIndent()
            )
        }

    @Test
    fun `emitsExactly fails when items emitted by the Flow are out of the expected order`() =
        testScope.runBlockingTest {
            val flow = flow {
                emit(1)
                delay(100)
                emit(3)
                delay(100)
                emit(2)
            }

            val exception = assertThrows<AssertionError> {
                assertThat(flow).emitsExactly(1, 2, 3)
            }
            assertThat(exception).hasMessageThat().contains(
                """
                    Flow did not emit exactly expected items
                    contents match, but order was wrong
                    expected: [1, 2, 3]
                    but was : [1, 3, 2]
                """.trimIndent()
            )
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

            val exception = assertThrows<AssertionError> {
                assertThat(flow).emitsExactly(1, 2, 3)
            }
            assertThat(exception).hasMessageThat().contains(
                """
                    Too many emissions from the Flow (only first unexpected emission is shown)
                    expected: [1, 2, 3]
                    but was : [1, 2, 3, 4]
                """.trimIndent()
            )
        }
}
