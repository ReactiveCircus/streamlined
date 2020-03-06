package io.github.reactivecircus.streamlined.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchPersonalizedStoriesTest {

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

    private val fetchPersonalizedStories = FetchPersonalizedStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `personalized stories fetched from repository are returned`() = runBlockingTest {
        val query = "query"
        coEvery { storyRepository.fetchPersonalizedStories(query) } returns dummyPersonalizedStoryList

        val result = fetchPersonalizedStories
            .execute(FetchPersonalizedStories.Params(query))

        assertThat(result)
            .isEqualTo(dummyPersonalizedStoryList)

        coVerify { storyRepository.fetchPersonalizedStories(query) }
    }
}
