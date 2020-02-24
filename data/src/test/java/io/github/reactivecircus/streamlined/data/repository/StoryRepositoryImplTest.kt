package io.github.reactivecircus.streamlined.data.repository

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.assertThat
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StoryRepositoryImplTest {

    private val dummyHeadlineStoryList = listOf(
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

    private val dummyPersonalizedStoryList = listOf(
        Story(
            id = 3,
            source = "source3",
            title = "Article 3",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 3000L
        ),
        Story(
            id = 4,
            source = "source4",
            title = "Article 4",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 4000L
        )
    )

    private val headlineStoryStore = mockk<HeadlineStoryStore>()

    private val personalizedStoryStore = mockk<PersonalizedStoryStore>()

    private val storyDao = mockk<StoryDao>()

    private val storyRepository = StoryRepositoryImpl(
        headlineStoryStore,
        personalizedStoryStore,
        storyDao
    )

    @Test
    fun `streamHeadlineStories() starts streaming from Store and fetches from API immediately`() =
        runBlockingTest {
            every { headlineStoryStore.stream(any()) } returns flowOf(
                StoreResponse.Data(dummyHeadlineStoryList, ResponseOrigin.Fetcher)
            )

            assertThat(storyRepository.streamHeadlineStories()).emitsExactly(
                StoreResponse.Data(dummyHeadlineStoryList, ResponseOrigin.Fetcher)
            )

            verifyAll {
                headlineStoryStore.stream(StoreRequest.cached(Unit, refresh = true))
            }
        }

    @Test
    fun `streamPersonalizedStories() starts streaming from Store and fetches from API immediately`() =
        runBlockingTest {
            every { personalizedStoryStore.stream(any()) } returns flowOf(
                StoreResponse.Data(dummyPersonalizedStoryList, ResponseOrigin.Persister)
            )

            assertThat(storyRepository.streamPersonalizedStories("query")).emitsExactly(
                StoreResponse.Data(dummyPersonalizedStoryList, ResponseOrigin.Persister)
            )

            verifyAll {
                personalizedStoryStore.stream(StoreRequest.cached("query", refresh = true))
            }
        }

    @Test
    fun `storyById() gets Story from DAO`() = runBlockingTest {
        val dummyStoryEntity = StoryEntity.Impl(
            id = 1,
            title = "Article",
            source = "source",
            author = "author",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1234L
        )

        every { storyDao.storyById(1) } returns dummyStoryEntity

        assertThat(storyRepository.getStoryById(1))
            .isEqualTo(dummyStoryEntity.toModel())
    }
}
