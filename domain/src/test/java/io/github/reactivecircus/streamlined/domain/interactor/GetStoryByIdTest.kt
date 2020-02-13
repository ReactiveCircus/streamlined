package io.github.reactivecircus.streamlined.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.assertThrows
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetStoryByIdTest {
    private val dummyStory = Story(
        id = 1,
        title = "Article 1",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 1000L
    )

    private val storyRepository = mockk<StoryRepository>()

    private val getStoryById = GetStoryById(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `getStoryById from repository when story is found`() = runBlockingTest {
        val id = 1L
        coEvery { storyRepository.getStoryById(id) } returns dummyStory

        assertThat(getStoryById.execute(GetStoryById.Params(id)))
            .isEqualTo(dummyStory)

        coVerifyAll {
            storyRepository.getStoryById(id)
        }
    }

    @Test
    fun `getStoryById from repository when story is not found`() = runBlockingTest {
        val id = 1L
        coEvery { storyRepository.getStoryById(id) } returns null

        val exception = assertThrows<NoSuchElementException> {
            getStoryById.execute(GetStoryById.Params(id))
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Could not find story for id $id")

        coVerifyAll {
            storyRepository.getStoryById(id)
        }
    }
}
