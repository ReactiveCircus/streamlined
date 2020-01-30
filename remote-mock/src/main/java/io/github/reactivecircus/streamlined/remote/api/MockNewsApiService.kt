package io.github.reactivecircus.streamlined.remote.api

import io.github.reactivecircus.streamlined.remote.dto.StoryDTO
import io.github.reactivecircus.streamlined.remote.dto.StoryListResponse
import retrofit2.mock.BehaviorDelegate

class MockNewsApiService(
    private val delegate: BehaviorDelegate<NewsApiService>
) : NewsApiService {

    override suspend fun headlines(country: String): StoryListResponse {
        // TODO MockData
        return delegate.returningResponse(emptyList<StoryDTO>())
            .headlines(country)
    }

    override suspend fun everything(): StoryListResponse {
        // TODO MockData
        return delegate.returningResponse(emptyList<StoryDTO>())
            .everything()
    }
}
