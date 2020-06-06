package io.github.reactivecircus.streamlined.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.assertThrows
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetStoryByIdTest {

    private val storyRepository = FakeStoryRepository()

    private val getStoryById = GetStoryById(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `getStoryById from repository when story is found`() = runBlockingTest {
        val id = FakeStoryRepository.DUMMY_HEADLINE_STORY_LIST[0].id

        assertThat(getStoryById.execute(GetStoryById.Params(id)))
            .isEqualTo(FakeStoryRepository.DUMMY_HEADLINE_STORY_LIST[0])
    }

    @Test
    fun `getStoryById from repository when story is not found`() = runBlockingTest {
        val id = 100L

        val exception = assertThrows<NoSuchElementException> {
            getStoryById.execute(GetStoryById.Params(id))
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Could not find story for id $id")
    }
}
