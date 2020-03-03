package io.github.reactivecircus.streamlined.domain.interactor

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.coroutines.test.ext.assertThat
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StreamPersonalizedStoriesTest {

    private val dummyPersonalizedStoryList = listOf(
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

    private val storyRepository = mockk<StoryRepository>()

    private val streamPersonalizedStories = StreamPersonalizedStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `streamPersonalizedStories from repository`() = runBlockingTest {
        val query = "query"
        val refresh = true
        every { storyRepository.streamPersonalizedStories(query, refresh) } returns flowOf(
            StoreResponse.Data(dummyPersonalizedStoryList, ResponseOrigin.Fetcher)
        )

        val params = StreamPersonalizedStories.Params(query, refresh)
        assertThat(streamPersonalizedStories.buildFlow(params)).emitsExactly(
            StoreResponse.Data(dummyPersonalizedStoryList, ResponseOrigin.Fetcher)
        )

        verifyAll {
            storyRepository.streamPersonalizedStories(query, refresh)
        }
    }
}
