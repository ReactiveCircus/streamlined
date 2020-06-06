package io.github.reactivecircus.streamlined.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchPersonalizedStoriesTest {

    private val storyRepository = FakeStoryRepository()

    private val fetchPersonalizedStories = FetchPersonalizedStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `personalized stories fetched from repository are returned`() = runBlockingTest {
        val query = "query"

        val result = fetchPersonalizedStories
            .execute(FetchPersonalizedStories.Params(query))

        assertThat(result)
            .isEqualTo(FakeStoryRepository.DUMMY_PERSONALIZED_STORY_LIST)
    }
}
