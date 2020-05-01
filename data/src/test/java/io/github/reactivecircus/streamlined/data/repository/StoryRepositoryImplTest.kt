package io.github.reactivecircus.streamlined.data.repository

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.nonFlowValueFetcher
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.FlowRecorder
import io.github.reactivecircus.coroutines.test.ext.recordWith
import io.github.reactivecircus.store.ext.RefreshPolicy
import io.github.reactivecircus.streamlined.data.TimeBasedRefreshPolicy
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.data.testutil.TestStoryDao
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.collections.set
import kotlin.time.ExperimentalTime
import kotlin.time.TestTimeSource
import kotlin.time.seconds

@ExperimentalTime
@ExperimentalStdlibApi
@FlowPreview
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

    private val dummyStoryEntity = StoryEntity.Impl(
        id = 1,
        title = "Article",
        source = "source",
        author = "author",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 1234L,
        isHeadline = false
    )

    private val testScope = TestCoroutineScope()

    private val flowRecorder = FlowRecorder<StoreResponse<List<Story>>>(testScope)

    private val headlineStoryFetcher = TestFetcher<Unit, List<Story>>(
        dummyHeadlineStoryList
    )

    private val headlineStoryPersister = InMemoryPersister<Unit, List<Story>>()

    private val headlineStoryStore = buildStore(
        fetcher = headlineStoryFetcher,
        persister = headlineStoryPersister,
        scope = testScope
    )

    private val personalizedStoryFetcher = TestFetcher<String, List<Story>>(
        dummyPersonalizedStoryList
    )

    private val personalizedStoryPersister = InMemoryPersister<String, List<Story>>()

    private val personalizedStoryStore = buildStore(
        fetcher = personalizedStoryFetcher,
        persister = personalizedStoryPersister,
        scope = testScope
    )

    private val freshDataExpiration = 10.seconds

    private val clock = TestTimeSource()

    private val refreshPolicy: RefreshPolicy = TimeBasedRefreshPolicy(
        expiration = freshDataExpiration,
        timeSource = clock
    )

    private val storyDao = TestStoryDao(
        expectedStoryByIdResult = dummyStoryEntity
    )

    private val storyRepository = StoryRepositoryImpl(
        headlineStoryStore,
        personalizedStoryStore,
        refreshPolicy,
        storyDao
    )

    @Test
    fun `streamHeadlineStories() streams data with StoreRequest#cached with forced initial refresh when no fetch was ever recorded`() =
        runBlockingTest {
            // given that cached data is available
            headlineStoryPersister.write(Unit, dummyHeadlineStoryList)

            storyRepository.streamHeadlineStories().recordWith(flowRecorder)
            assertThat(flowRecorder.takeAll())
                .containsExactly(
                    StoreResponse.Data(dummyHeadlineStoryList, ResponseOrigin.SourceOfTruth),
                    StoreResponse.Loading<List<Story>>(ResponseOrigin.Fetcher),
                    StoreResponse.Data(dummyHeadlineStoryList, ResponseOrigin.Fetcher)
                )

            // fetcher should still have been invoked despite have a cache
            assertThat(headlineStoryFetcher.fetchCount)
                .isEqualTo(1)
        }

    @Test
    fun `streamHeadlineStories() streams data with StoreRequest#cached without forced initial refresh when previously recorded fetch is unexpired`() =
        runBlockingTest {
            // given that cached data is available
            headlineStoryPersister.write(Unit, dummyHeadlineStoryList)

            storyRepository.streamHeadlineStories().recordWith(flowRecorder)

            // fetcher should have been invoked first time
            assertThat(headlineStoryFetcher.fetchCount)
                .isEqualTo(1)

            storyRepository.streamHeadlineStories().recordWith(flowRecorder)

            // fetcher should not have been invoked again as unexpired fetch was recorded
            assertThat(headlineStoryFetcher.fetchCount)
                .isEqualTo(1)

            clock += 9.seconds

            storyRepository.streamHeadlineStories().recordWith(flowRecorder)

            // fetcher should not have been invoked again previous fetch is yet to expire
            assertThat(headlineStoryFetcher.fetchCount)
                .isEqualTo(1)

            clock += 1.seconds

            storyRepository.streamHeadlineStories().recordWith(flowRecorder)

            // fetcher should have been invoked again after previous fetch expired
            assertThat(headlineStoryFetcher.fetchCount)
                .isEqualTo(2)
        }

    @Test
    fun `streamPersonalizedStories() streams data with StoreRequest#cached with forced initial refresh when no fetch was ever recorded`() =
        runBlockingTest {
            val query = "query"

            // given that cached data is available
            personalizedStoryPersister.write(query, dummyPersonalizedStoryList)

            storyRepository.streamPersonalizedStories(query).recordWith(flowRecorder)
            assertThat(flowRecorder.takeAll())
                .containsExactly(
                    StoreResponse.Data(dummyPersonalizedStoryList, ResponseOrigin.SourceOfTruth),
                    StoreResponse.Loading<List<Story>>(ResponseOrigin.Fetcher),
                    StoreResponse.Data(dummyPersonalizedStoryList, ResponseOrigin.Fetcher)
                )

            // fetcher should still have been invoked despite have a cache
            assertThat(personalizedStoryFetcher.fetchCount)
                .isEqualTo(1)
        }

    @Test
    fun `streamPersonalizedStories() streams data with StoreRequest#cached without forced initial refresh when previously recorded fetch is unexpired`() =
        runBlockingTest {
            val query = "query"

            // given that cached data is available
            personalizedStoryPersister.write(query, dummyPersonalizedStoryList)

            storyRepository.streamPersonalizedStories(query).recordWith(flowRecorder)

            // fetcher should have been invoked first time
            assertThat(personalizedStoryFetcher.fetchCount)
                .isEqualTo(1)

            storyRepository.streamPersonalizedStories(query).recordWith(flowRecorder)

            // fetcher should not have been invoked again as unexpired fetch was recorded
            assertThat(personalizedStoryFetcher.fetchCount)
                .isEqualTo(1)

            clock += 9.seconds

            storyRepository.streamPersonalizedStories(query).recordWith(flowRecorder)

            // fetcher should not have been invoked again previous fetch is yet to expire
            assertThat(personalizedStoryFetcher.fetchCount)
                .isEqualTo(1)

            clock += 1.seconds

            storyRepository.streamPersonalizedStories(query).recordWith(flowRecorder)

            // fetcher should have been invoked again after previous fetch expired
            assertThat(personalizedStoryFetcher.fetchCount)
                .isEqualTo(2)
        }

    @Test
    fun `fetchHeadlineStories() fetches fresh data from API via Store`() = runBlockingTest {
        assertThat(storyRepository.fetchHeadlineStories())
            .isEqualTo(dummyHeadlineStoryList)

        assertThat(headlineStoryFetcher.fetchCount)
            .isEqualTo(1)
    }

    @Test
    fun `fetchPersonalizedStories() fetches fresh data from API via Store`() = runBlockingTest {
        val query = "query"

        assertThat(storyRepository.fetchPersonalizedStories(query))
            .isEqualTo(dummyPersonalizedStoryList)

        assertThat(personalizedStoryFetcher.fetchCount)
            .isEqualTo(1)
    }

    @Test
    fun `storyById() gets Story from DAO`() = runBlockingTest {
        assertThat(storyRepository.getStoryById(1))
            .isEqualTo(dummyStoryEntity.toModel())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun <Key : Any, Output : Any> buildStore(
    fetcher: TestFetcher<Key, Output>,
    persister: InMemoryPersister<Key, Output>,
    scope: CoroutineScope
): Store<Key, Output> {
    return StoreBuilder.from(
        fetcher = nonFlowValueFetcher(fetcher::fetch),
        sourceOfTruth = SourceOfTruth.fromNonFlow(
            reader = persister::read,
            writer = persister::write
        )
    )
        .disableCache()
        .scope(scope)
        .build()
}

class InMemoryPersister<Key : Any, Output : Any> {

    private val data = mutableMapOf<Key, Output>()

    @Suppress("RedundantSuspendModifier")
    suspend fun read(key: Key): Output? = data[key]

    @Suppress("RedundantSuspendModifier")
    suspend fun write(key: Key, output: Output) {
        data[key] = output
    }
}

class TestFetcher<Key : Any, Output : Any>(
    private val expectedResponse: Output
) {
    private var _fetchCount = 0

    val fetchCount get() = _fetchCount

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    suspend fun fetch(key: Key): Output {
        _fetchCount++
        return expectedResponse
    }
}
