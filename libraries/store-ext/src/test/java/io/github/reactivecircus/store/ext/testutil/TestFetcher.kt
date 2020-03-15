package io.github.reactivecircus.store.ext.testutil

class TestFetcher<Key : Any, Output : Any>(
    private vararg val scheduledResponses: FetcherResponse<Output>
) {
    private var index = 0

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    suspend fun fetch(key: Key): Output {
        if (index >= scheduledResponses.size) {
            throw AssertionError("unexpected fetch request")
        }
        when (val response = scheduledResponses[index++]) {
            is FetcherResponse.Success<Output> -> return response.output
            is FetcherResponse.Error -> throw response.exception
        }
    }
}

sealed class FetcherResponse<Output : Any> {
    data class Success<Output : Any>(val output: Output) : FetcherResponse<Output>()
    data class Error<Output : Any>(val exception: Exception) : FetcherResponse<Output>()
}
