@file:Suppress("MatchingDeclarationName")

package io.github.reactivecircus.coroutines.test.ext

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineScope

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> TestCoroutineScope.assertThat(flow: Flow<T>): FlowSubject<T> {
    return assertAbout(
        FlowSubject.Factory<T>(
            this
        )
    ).that(flow)
}

@UseExperimental(ExperimentalCoroutinesApi::class)
class FlowSubject<T> constructor(
    failureMetadata: FailureMetadata,
    private val testCoroutineScope: TestCoroutineScope,
    private val actual: Flow<T>
) : Subject(failureMetadata, actual) {

    /**
     * Takes all items in the flow that are available by collecting on it as long as there are
     * active jobs in the given [TestCoroutineScope].
     *
     * It ensures all expected items are dispatched as well as no additional unexpected items are
     * dispatched.
     */
    @UseExperimental(InternalCoroutinesApi::class)
    suspend fun emitsExactly(vararg expected: T) {
        val collectedSoFar = mutableListOf<T>()
        val collectJob = testCoroutineScope.async {
            actual.collect {
                collectedSoFar.add(it)
                assertThat(collectedSoFar.size).isAtMost(expected.size)
            }
        }
        testCoroutineScope.advanceUntilIdle()
        collectJob.cancel()
        assertThat(collectedSoFar).isEqualTo(expected.toList())
    }

    class Factory<T>(
        private val testCoroutineScope: TestCoroutineScope
    ) : Subject.Factory<FlowSubject<T>, Flow<T>> {
        override fun createSubject(metadata: FailureMetadata, actual: Flow<T>): FlowSubject<T> {
            return FlowSubject(
                failureMetadata = metadata,
                actual = actual,
                testCoroutineScope = testCoroutineScope
            )
        }
    }
}
