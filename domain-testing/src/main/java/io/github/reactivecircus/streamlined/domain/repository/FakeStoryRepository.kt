package io.github.reactivecircus.streamlined.domain.repository

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * A fake implementation of [StoryRepository] that supports scheduling future responses.
 */
class FakeStoryRepository : StoryRepository {

    private var nextStreamHeadlineStoriesFlow: Flow<StoreResponse<List<Story>>> =
        DEFAULT_HEADLINE_STORIES_FLOW

    private var nextStreamPersonalizedStoriesFlow: Flow<StoreResponse<List<Story>>> =
        DEFAULT_PERSONALIZED_STORIES_FLOW

    private var nextFetchHeadlineStoriesResponse: FakeResponse<List<Story>> =
        DEFAULT_FETCH_HEADLINE_STORIES_RESPONSE

    private var nextFetchPersonalizedStoriesResponse: FakeResponse<List<Story>> =
        DEFAULT_FETCH_PERSONALIZED_STORIES_RESPONSE

    override fun streamHeadlineStories(): Flow<StoreResponse<List<Story>>> {
        return nextStreamHeadlineStoriesFlow
    }

    override fun streamPersonalizedStories(query: String): Flow<StoreResponse<List<Story>>> {
        return nextStreamPersonalizedStoriesFlow
    }

    override suspend fun fetchHeadlineStories(): List<Story> {
        when (val nextResponse = nextFetchHeadlineStoriesResponse) {
            is FakeResponse.Success<List<Story>> -> return nextResponse.result
            is FakeResponse.Error -> throw nextResponse.exception
        }
    }

    override suspend fun fetchPersonalizedStories(query: String): List<Story> {
        when (val nextResponse = nextFetchPersonalizedStoriesResponse) {
            is FakeResponse.Success<List<Story>> -> return nextResponse.result
            is FakeResponse.Error -> throw nextResponse.exception
        }
    }

    override suspend fun getStoryById(id: Long): Story? {
        return (DUMMY_HEADLINE_STORY_LIST + DUMMY_PERSONALIZED_STORY_LIST)
            .find { it.id == id }
    }

    fun scheduleNext(scheduleNextResponses: NextResponsesBuilder.() -> Unit) {
        val builder = NextResponsesBuilder().apply {
            scheduleNextResponses()
        }
        builder.build().run {
            nextStreamHeadlineStoriesFlow = headlineStoriesFlow
            nextStreamPersonalizedStoriesFlow = personalizedStoriesFlow
            nextFetchHeadlineStoriesResponse = fetchHeadlineStoriesResponse
            nextFetchPersonalizedStoriesResponse = fetchPersonalizedStoriesResponse
        }
    }

    class NextResponsesBuilder {

        private var headlineStoriesFlow: Flow<StoreResponse<List<Story>>>? = null
        private var personalizedStoriesFlow: Flow<StoreResponse<List<Story>>>? = null
        private var fetchHeadlineStoriesResponse: FakeResponse<List<Story>>? = null
        private var fetchPersonalizedStoriesResponse: FakeResponse<List<Story>>? = null

        fun nextHeadlineStoriesFlow(responseFlow: Flow<StoreResponse<List<Story>>>) {
            headlineStoriesFlow = responseFlow
        }

        fun nextPersonalizedStoriesFlow(responseFlow: Flow<StoreResponse<List<Story>>>) {
            personalizedStoriesFlow = responseFlow
        }

        fun nextFetchHeadlineStoriesResponse(response: FakeResponse<List<Story>>) {
            fetchHeadlineStoriesResponse = response
        }

        fun nextFetchPersonalizedStoriesResponse(response: FakeResponse<List<Story>>) {
            fetchPersonalizedStoriesResponse = response
        }

        fun build() = ScheduledResponses(
            headlineStoriesFlow = headlineStoriesFlow ?: DEFAULT_HEADLINE_STORIES_FLOW,
            personalizedStoriesFlow = personalizedStoriesFlow ?: DEFAULT_PERSONALIZED_STORIES_FLOW,
            fetchHeadlineStoriesResponse = fetchHeadlineStoriesResponse
                ?: DEFAULT_FETCH_HEADLINE_STORIES_RESPONSE,
            fetchPersonalizedStoriesResponse = fetchPersonalizedStoriesResponse
                ?: DEFAULT_FETCH_PERSONALIZED_STORIES_RESPONSE
        )
    }

    class ScheduledResponses(
        val headlineStoriesFlow: Flow<StoreResponse<List<Story>>>,
        val personalizedStoriesFlow: Flow<StoreResponse<List<Story>>>,
        val fetchHeadlineStoriesResponse: FakeResponse<List<Story>>,
        val fetchPersonalizedStoriesResponse: FakeResponse<List<Story>>
    )

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
            ),
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
            ),
        )

        private val DEFAULT_HEADLINE_STORIES_FLOW = flowOf(
            StoreResponse.Data(DUMMY_HEADLINE_STORY_LIST, ResponseOrigin.Fetcher)
        )

        private val DEFAULT_PERSONALIZED_STORIES_FLOW = flowOf(
            StoreResponse.Data(DUMMY_PERSONALIZED_STORY_LIST, ResponseOrigin.Fetcher)
        )

        private val DEFAULT_FETCH_HEADLINE_STORIES_RESPONSE = FakeResponse.Success(
            DUMMY_HEADLINE_STORY_LIST
        )

        private val DEFAULT_FETCH_PERSONALIZED_STORIES_RESPONSE = FakeResponse.Success(
            DUMMY_PERSONALIZED_STORY_LIST
        )
    }
}
