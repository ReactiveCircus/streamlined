package io.github.reactivecircus.streamlined.domain.interactor

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeStoryRepository : StoryRepository {
    override fun streamHeadlineStories(): Flow<StoreResponse<List<Story>>> {
        return flowOf(StoreResponse.Data(DUMMY_HEADLINE_STORY_LIST, ResponseOrigin.Fetcher))
    }

    override fun streamPersonalizedStories(query: String): Flow<StoreResponse<List<Story>>> {
        return flowOf(StoreResponse.Data(DUMMY_PERSONALIZED_STORY_LIST, ResponseOrigin.Fetcher))
    }

    override suspend fun fetchHeadlineStories(): List<Story> {
        return DUMMY_HEADLINE_STORY_LIST
    }

    override suspend fun fetchPersonalizedStories(query: String): List<Story> {
        return DUMMY_PERSONALIZED_STORY_LIST
    }

    override suspend fun getStoryById(id: Long): Story? {
        return (DUMMY_HEADLINE_STORY_LIST + DUMMY_PERSONALIZED_STORY_LIST)
            .find { it.id == id }
    }

    companion object {
        val DUMMY_HEADLINE_STORY_LIST = listOf(
            Story(
                id = 1,
                source = "source1",
                title = "Article 1",
                author = "Yang",
                description = "Description...",
                url = "url",
                imageUrl = "image-url",
                publishedTime = 1000L
            ),
            Story(
                id = 2,
                source = "source2",
                title = "Article 2",
                author = "Yang",
                description = "Description...",
                url = "url",
                imageUrl = "image-url",
                publishedTime = 2000L
            )
        )

        val DUMMY_PERSONALIZED_STORY_LIST = listOf(
            Story(
                id = 3,
                source = "source2",
                title = "Article 3",
                author = "Yang",
                description = "Description...",
                url = "url",
                imageUrl = "image-url",
                publishedTime = 3000L
            ),
            Story(
                id = 4,
                source = "source4",
                title = "Article 4",
                author = "Yang",
                description = "Description...",
                url = "url",
                imageUrl = "image-url",
                publishedTime = 4000L
            )
        )
    }
}
