package io.github.reactivecircus.streamlined.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams

@ExperimentalCoroutinesApi
class FetchHeadlineStoriesTest {

    private val storyRepository = FakeStoryRepository()

    private val fetchHeadlineStories = FetchHeadlineStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `headline stories fetched from repository are returned`() =
        runBlockingTest {
            val result = fetchHeadlineStories.execute(EmptyParams)

            assertThat(result)
                .isEqualTo(FakeStoryRepository.DUMMY_HEADLINE_STORY_LIST)
        }
}
