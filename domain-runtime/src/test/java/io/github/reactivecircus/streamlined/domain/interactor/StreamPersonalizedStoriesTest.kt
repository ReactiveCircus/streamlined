package io.github.reactivecircus.streamlined.domain.interactor

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.coroutines.test.ext.assertThat
import io.github.reactivecircus.streamlined.domain.repository.FakeStoryRepository
import io.github.reactivecircus.streamlined.domain.testCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StreamPersonalizedStoriesTest {

    private val storyRepository = FakeStoryRepository()

    private val streamPersonalizedStories = StreamPersonalizedStories(
        storyRepository = storyRepository,
        dispatcherProvider = testCoroutineDispatcherProvider
    )

    @Test
    fun `streamPersonalizedStories from repository`() = runBlockingTest {
        val query = "query"

        val params = StreamPersonalizedStories.Params(query)
        assertThat(streamPersonalizedStories.buildFlow(params)).emitsExactly(
            StoreResponse.Data(
                FakeStoryRepository.DUMMY_PERSONALIZED_STORY_LIST,
                ResponseOrigin.Fetcher
            )
        )
    }
}
