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
import reactivecircus.blueprint.interactor.EmptyParams

@ExperimentalCoroutinesApi
class StreamHeadlineStoriesTest {

    private val dummyHeadlineStoryList = listOf(
        Story(
            id = 1,
            title = "Article 1",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1000L
        ),
        Story(
            id = 2,
            title = "Article 2",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 2000L
        )
    )

    private val storyRepository = mockk<StoryRepository>()

    private val streamHeadlineStories = StreamHeadlineStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `streamHeadlineStories from repository`() = runBlockingTest {
        every { storyRepository.streamHeadlineStories() } returns flowOf(
            StoreResponse.Data(dummyHeadlineStoryList, ResponseOrigin.Fetcher)
        )

        assertThat(streamHeadlineStories.buildFlow(EmptyParams)).emitsExactly(
            StoreResponse.Data(dummyHeadlineStoryList, ResponseOrigin.Fetcher)
        )

        verifyAll {
            storyRepository.streamHeadlineStories()
        }
    }
}
