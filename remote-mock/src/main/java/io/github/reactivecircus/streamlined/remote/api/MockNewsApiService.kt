package io.github.reactivecircus.streamlined.remote.api

import io.github.reactivecircus.streamlined.remote.MockData
import io.github.reactivecircus.streamlined.remote.dto.StoryListResponse
import retrofit2.mock.BehaviorDelegate

class MockNewsApiService(
    private val delegate: BehaviorDelegate<NewsApiService>
) : NewsApiService {

    override suspend fun headlines(country: String): StoryListResponse {
        val headlineStoriesResponse = StoryListResponse(
            totalResults = MockData.mockHeadlineStories.size,
            stories = MockData.mockHeadlineStories
        )
        return delegate.returningResponse(headlineStoriesResponse)
            .headlines(country)
    }

    override suspend fun everything(): StoryListResponse {
        val allStoriesResponse = StoryListResponse(
            totalResults = MockData.mockAllStories.size,
            stories = MockData.mockAllStories
        )
        return delegate.returningResponse(allStoriesResponse)
            .everything()
    }
}
