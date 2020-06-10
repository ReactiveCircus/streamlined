package io.github.reactivecircus.streamlined.domain.interactor

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.coroutines.test.ext.assertThat
import io.github.reactivecircus.streamlined.domain.repository.FakeStoryRepository
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams

@ExperimentalCoroutinesApi
class StreamHeadlineStoriesTest {

    private val storyRepository = FakeStoryRepository()

    private val streamHeadlineStories = StreamHeadlineStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `streamHeadlineStories from repository`() = runBlockingTest {
        assertThat(streamHeadlineStories.buildFlow(EmptyParams)).emitsExactly(
            StoreResponse.Data(
                FakeStoryRepository.DUMMY_HEADLINE_STORY_LIST,
                ResponseOrigin.Fetcher
            )
        )
    }
}
