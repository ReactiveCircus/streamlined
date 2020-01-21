package io.github.reactivecircus.streamlined.remote.api

import io.github.reactivecircus.streamlined.remote.dto.StoryDTO
import retrofit2.mock.BehaviorDelegate

internal class MockNewsApiService(
    private val delegate: BehaviorDelegate<NewsApiService>
) : NewsApiService {

    override suspend fun headlines(): List<StoryDTO> {
        // TODO MockData
        return delegate.returningResponse(emptyList<StoryDTO>())
            .headlines()
    }

    override suspend fun everything(): List<StoryDTO> {
        // TODO MockData
        return delegate.returningResponse(emptyList<StoryDTO>())
            .everything()
    }
}
