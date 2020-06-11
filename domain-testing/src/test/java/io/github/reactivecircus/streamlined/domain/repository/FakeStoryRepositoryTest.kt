package io.github.reactivecircus.streamlined.domain.repository

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.FlowRecorder
import io.github.reactivecircus.coroutines.test.ext.assertThrows
import io.github.reactivecircus.coroutines.test.ext.recordWith
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.FakeStoryRepository.Companion.DUMMY_HEADLINE_STORY_LIST
import io.github.reactivecircus.streamlined.domain.repository.FakeStoryRepository.Companion.DUMMY_PERSONALIZED_STORY_LIST
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class FakeStoryRepositoryTest {

    private val testScope = TestCoroutineScope()
    private val fakeStoryRepository = FakeStoryRepository()

    @Test
    fun `streamHeadlineStories returns flow of single StoreResponse of DUMMY_HEADLINE_STORY_LIST by default`() {
        val recorder = FlowRecorder<StoreResponse<List<Story>>>(testScope)
        fakeStoryRepository.streamHeadlineStories().recordWith(recorder)

        assertThat(recorder.takeAll())
            .containsExactly(
                StoreResponse.Data(DUMMY_HEADLINE_STORY_LIST, ResponseOrigin.Fetcher)
            )
    }

    @Test
    fun `streamHeadlineStories returns scheduled Flow after scheduling`() {
        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(
                flowOf(StoreResponse.Data(emptyList(), ResponseOrigin.Cache))
            )
        }

        val recorder = FlowRecorder<StoreResponse<List<Story>>>(testScope)
        fakeStoryRepository.streamHeadlineStories().recordWith(recorder)

        assertThat(recorder.takeAll())
            .containsExactly(
                StoreResponse.Data(emptyList<Story>(), ResponseOrigin.Cache)
            )
    }

    @Test
    fun `streamPersonalizedStories returns flow of single StoreResponse of DUMMY_PERSONALIZED_STORY_LIST by default`() {
        val recorder = FlowRecorder<StoreResponse<List<Story>>>(testScope)
        fakeStoryRepository.streamPersonalizedStories("").recordWith(recorder)

        assertThat(recorder.takeAll())
            .containsExactly(
                StoreResponse.Data(
                    DUMMY_PERSONALIZED_STORY_LIST,
                    ResponseOrigin.Fetcher
                )
            )
    }

    @Test
    fun `streamPersonalizedStories returns scheduled Flow after scheduling`() {
        fakeStoryRepository.scheduleNext {
            nextPersonalizedStoriesFlow(
                flowOf(StoreResponse.Data(emptyList(), ResponseOrigin.SourceOfTruth))
            )
        }

        val recorder = FlowRecorder<StoreResponse<List<Story>>>(testScope)
        fakeStoryRepository.streamPersonalizedStories("").recordWith(recorder)

        assertThat(recorder.takeAll())
            .containsExactly(
                StoreResponse.Data(emptyList<Story>(), ResponseOrigin.SourceOfTruth)
            )
    }

    @Test
    fun `fetchHeadlineStories returns DUMMY_HEADLINE_STORY_LIST by default`() =
        runBlockingTest {
            assertThat(fakeStoryRepository.fetchHeadlineStories())
                .isEqualTo(DUMMY_HEADLINE_STORY_LIST)
        }

    @Test
    fun `fetchHeadlineStories returns scheduled result after scheduling a Success response`() =
        runBlockingTest {
            fakeStoryRepository.scheduleNext {
                nextFetchHeadlineStoriesResponse(FakeResponse.Success(emptyList()))
            }

            assertThat(fakeStoryRepository.fetchHeadlineStories())
                .isEqualTo(emptyList<Story>())
        }

    @Test
    fun `fetchHeadlineStories throws scheduled exception after scheduling a Error response`() =
        runBlockingTest {
            fakeStoryRepository.scheduleNext {
                nextFetchHeadlineStoriesResponse(FakeResponse.Error(IOException("error")))
            }

            val exception = assertThrows<IOException> {
                fakeStoryRepository.fetchHeadlineStories()
            }

            assertThat(exception)
                .hasMessageThat()
                .isEqualTo("error")
        }

    @Test
    fun `fetchPersonalizedStories returns DUMMY_PERSONALIZED_STORY_LIST by default`() =
        runBlockingTest {
            assertThat(fakeStoryRepository.fetchPersonalizedStories(""))
                .isEqualTo(DUMMY_PERSONALIZED_STORY_LIST)
        }

    @Test
    fun `fetchPersonalizedStories returns scheduled result after scheduling a Success response`() =
        runBlockingTest {
            fakeStoryRepository.scheduleNext {
                nextFetchPersonalizedStoriesResponse(FakeResponse.Success(emptyList()))
            }

            assertThat(fakeStoryRepository.fetchPersonalizedStories(""))
                .isEqualTo(emptyList<Story>())
        }

    @Test
    fun `fetchPersonalizedStories throws scheduled exception after scheduling a Error response`() =
        runBlockingTest {
            fakeStoryRepository.scheduleNext {
                nextFetchPersonalizedStoriesResponse(FakeResponse.Error(IOException("error")))
            }

            val exception = assertThrows<IOException> {
                fakeStoryRepository.fetchPersonalizedStories("")
            }

            assertThat(exception)
                .hasMessageThat()
                .isEqualTo("error")
        }

    @Test
    fun `getStoryById returns Story when story with matching id exists in DUMMY_HEADLINE_STORY_LIST or DUMMY_PERSONALIZED_STORY_LIST`() =
        runBlockingTest {
            assertThat(fakeStoryRepository.getStoryById(DUMMY_HEADLINE_STORY_LIST[0].id))
                .isEqualTo(DUMMY_HEADLINE_STORY_LIST[0])
            assertThat(fakeStoryRepository.getStoryById(DUMMY_PERSONALIZED_STORY_LIST[0].id))
                .isEqualTo(DUMMY_PERSONALIZED_STORY_LIST[0])
        }

    @Test
    fun `getStoryById returns null when no stories with matching id exist in DUMMY_HEADLINE_STORY_LIST or DUMMY_PERSONALIZED_STORY_LIST`() =
        runBlockingTest {
            assertThat(fakeStoryRepository.getStoryById(100))
                .isNull()
        }
}
