package io.github.reactivecircus.streamlined.domain.repository

sealed class FakeResponse<Output> {
    data class Success<Output>(val result: Output) : FakeResponse<Output>()
    data class Error<Output>(val exception: Exception) : FakeResponse<Output>()
}
