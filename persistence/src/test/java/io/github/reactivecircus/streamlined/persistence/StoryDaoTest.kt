package io.github.reactivecircus.streamlined.persistence

import com.google.common.truth.Truth.assertThat
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.github.reactivecircus.coroutines.test.ext.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StoryDaoTest {

    private val testScope = TestCoroutineScope()

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        StreamlinedDatabase.Schema.create(this)
    }

    private val queries = StreamlinedDatabase(inMemorySqlDriver).storyEntityQueries

    private val dao: StoryDao = StoryDaoImpl(queries, testScope.coroutineContext)

    private val headlineStory1 = StoryEntity.Impl(
        id = -1,
        source = "source1",
        title = "Article 1",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 1000L,
        isHeadline = true
    )

    private val headlineStory2 = StoryEntity.Impl(
        id = -1,
        source = "source2",
        title = "Article 2",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 1000L,
        isHeadline = true
    )

    private val nonHeadlineStory1 = StoryEntity.Impl(
        id = -1,
        source = "source3",
        title = "Article 3",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 2000L,
        isHeadline = false
    )

    private val nonHeadlineStory2 = StoryEntity.Impl(
        id = -1,
        source = "source4",
        title = "Article 4",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 2000L,
        isHeadline = false
    )

    @Test
    fun `allStories() emits all existing stories`() = runBlockingTest {
        assertThat(dao.allStories())
            .emitsExactly(emptyList())

        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory1.title,
            source = nonHeadlineStory1.source,
            author = nonHeadlineStory1.author,
            description = nonHeadlineStory1.description,
            url = nonHeadlineStory1.url,
            imageUrl = nonHeadlineStory1.imageUrl,
            publishedTime = nonHeadlineStory1.publishedTime,
            isHeadline = nonHeadlineStory1.isHeadline
        )

        assertThat(dao.allStories())
            .emitsExactly(
                listOf(headlineStory1.copy(id = 1), nonHeadlineStory1.copy(id = 2))
            )
    }

    @Test
    fun `headlineStories() emits all existing headline stories`() = runBlockingTest {
        assertThat(dao.headlineStories())
            .emitsExactly(emptyList())

        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory1.title,
            source = nonHeadlineStory1.source,
            author = nonHeadlineStory1.author,
            description = nonHeadlineStory1.description,
            url = nonHeadlineStory1.url,
            imageUrl = nonHeadlineStory1.imageUrl,
            publishedTime = nonHeadlineStory1.publishedTime,
            isHeadline = nonHeadlineStory1.isHeadline
        )

        assertThat(dao.headlineStories())
            .emitsExactly(
                listOf(headlineStory1.copy(id = 1))
            )
    }

    @Test
    fun `nonHeadlineStories() emits all existing non-headline stories`() = runBlockingTest {
        assertThat(dao.nonHeadlineStories())
            .emitsExactly(emptyList())

        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory1.title,
            source = nonHeadlineStory1.source,
            author = nonHeadlineStory1.author,
            description = nonHeadlineStory1.description,
            url = nonHeadlineStory1.url,
            imageUrl = nonHeadlineStory1.imageUrl,
            publishedTime = nonHeadlineStory1.publishedTime,
            isHeadline = nonHeadlineStory1.isHeadline
        )

        assertThat(dao.nonHeadlineStories())
            .emitsExactly(
                listOf(nonHeadlineStory1.copy(id = 2))
            )
    }

    @Test
    fun `storyById() returns existing story with matching id`() = runBlockingTest {
        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )

        assertThat(dao.storyById(1))
            .isEqualTo(headlineStory1.copy(id = 1))
    }

    @Test
    fun `storyById() returns null when no story with matching id exists`() = runBlockingTest {
        assertThat(dao.storyById(1))
            .isNull()
    }

    @Test
    fun `updateStories(forHeadlines = true) inserts new headline stories when no headline stories exist`() =
        runBlockingTest {
            dao.updateStories(
                forHeadlines = true,
                stories = listOf(headlineStory1, headlineStory2)
            )

            assertThat(queries.findAllStories().executeAsList())
                .isEqualTo(listOf(headlineStory1.copy(id = 1), headlineStory2.copy(id = 2)))
        }

    @Test
    fun `updateStories(forHeadlines = true) inserts or updates headline stories and deletes all existing headline stories`() =
        runBlockingTest {
            queries.insertStory(
                title = headlineStory1.title,
                source = headlineStory1.source,
                author = headlineStory1.author,
                description = headlineStory1.description,
                url = headlineStory1.url,
                imageUrl = headlineStory1.imageUrl,
                publishedTime = headlineStory1.publishedTime,
                isHeadline = headlineStory1.isHeadline
            )

            queries.insertStory(
                title = "old story",
                source = "source",
                author = "author",
                description = null,
                url = "url",
                imageUrl = null,
                publishedTime = 2000,
                isHeadline = true
            )

            dao.updateStories(
                forHeadlines = true,
                stories = listOf(
                    headlineStory1.copy(description = "New description"),
                    headlineStory2
                )
            )

            assertThat(queries.findAllStories().executeAsList())
                .isEqualTo(
                    listOf(
                        headlineStory1.copy(id = 1, description = "New description"),
                        headlineStory2.copy(id = 3)
                    )
                )
        }

    @Test
    fun `updateStories(forHeadlines = false) inserts new non-headline stories when no non-headline stories exist`() =
        runBlockingTest {
            dao.updateStories(
                forHeadlines = false,
                stories = listOf(nonHeadlineStory1, nonHeadlineStory2)
            )

            assertThat(queries.findAllStories().executeAsList())
                .isEqualTo(listOf(nonHeadlineStory1.copy(id = 1), nonHeadlineStory2.copy(id = 2)))
        }

    @Test
    fun `updateStories(forHeadlines = false) inserts or updates non-headline stories and deletes all existing non-headline stories`() =
        runBlockingTest {
            queries.insertStory(
                title = nonHeadlineStory1.title,
                source = nonHeadlineStory1.source,
                author = nonHeadlineStory1.author,
                description = nonHeadlineStory1.description,
                url = nonHeadlineStory1.url,
                imageUrl = nonHeadlineStory1.imageUrl,
                publishedTime = nonHeadlineStory1.publishedTime,
                isHeadline = nonHeadlineStory1.isHeadline
            )

            queries.insertStory(
                title = "old story",
                source = "source",
                author = "author",
                description = null,
                url = "url",
                imageUrl = null,
                publishedTime = 2000,
                isHeadline = false
            )

            dao.updateStories(
                forHeadlines = false,
                stories = listOf(
                    nonHeadlineStory1.copy(description = "New description"),
                    nonHeadlineStory2
                )
            )

            assertThat(queries.findAllStories().executeAsList())
                .isEqualTo(
                    listOf(
                        nonHeadlineStory1.copy(id = 1, description = "New description"),
                        nonHeadlineStory2.copy(id = 3)
                    )
                )
        }

    @Test
    fun `updateStories(forHeadlines = true) does not change existing non-headline stories to headline stories`() =
        runBlockingTest {
            queries.insertStory(
                title = nonHeadlineStory1.title,
                source = nonHeadlineStory1.source,
                author = nonHeadlineStory1.author,
                description = nonHeadlineStory1.description,
                url = nonHeadlineStory1.url,
                imageUrl = nonHeadlineStory1.imageUrl,
                publishedTime = nonHeadlineStory1.publishedTime,
                isHeadline = nonHeadlineStory1.isHeadline
            )

            dao.updateStories(
                forHeadlines = true,
                stories = listOf(
                    nonHeadlineStory1.copy(isHeadline = true),
                    headlineStory2
                )
            )

            assertThat(queries.findAllStories().executeAsList())
                .isEqualTo(
                    listOf(
                        nonHeadlineStory1.copy(id = 1),
                        headlineStory2.copy(id = 2)
                    )
                )
        }

    @Test
    fun `updateStories(forHeadlines = false) does not change existing headline stories to non-headline stories`() =
        runBlockingTest {
            queries.insertStory(
                title = headlineStory1.title,
                source = headlineStory1.source,
                author = headlineStory1.author,
                description = headlineStory1.description,
                url = headlineStory1.url,
                imageUrl = headlineStory1.imageUrl,
                publishedTime = headlineStory1.publishedTime,
                isHeadline = headlineStory1.isHeadline
            )

            dao.updateStories(
                forHeadlines = false,
                stories = listOf(
                    headlineStory1.copy(isHeadline = false),
                    nonHeadlineStory2
                )
            )

            assertThat(queries.findAllStories().executeAsList())
                .isEqualTo(
                    listOf(
                        headlineStory1.copy(id = 1),
                        nonHeadlineStory2.copy(id = 2)
                    )
                )
        }

    @Test
    fun `deleteAll() deletes all existing stories`() = runBlockingTest {
        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory1.title,
            source = nonHeadlineStory1.source,
            author = nonHeadlineStory1.author,
            description = nonHeadlineStory1.description,
            url = nonHeadlineStory1.url,
            imageUrl = nonHeadlineStory1.imageUrl,
            publishedTime = nonHeadlineStory1.publishedTime,
            isHeadline = nonHeadlineStory1.isHeadline
        )

        dao.deleteAll()

        assertThat(queries.findAllStories().executeAsList())
            .isEqualTo(emptyList<StoryEntity>())
    }

    @Test
    fun `deleteHeadlineStories() deletes all existing headline stories`() = runBlockingTest {
        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )
        queries.insertStory(
            title = headlineStory2.title,
            source = headlineStory2.source,
            author = headlineStory2.author,
            description = headlineStory2.description,
            url = headlineStory2.url,
            imageUrl = headlineStory2.imageUrl,
            publishedTime = headlineStory2.publishedTime,
            isHeadline = headlineStory2.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory1.title,
            source = nonHeadlineStory1.source,
            author = nonHeadlineStory1.author,
            description = nonHeadlineStory1.description,
            url = nonHeadlineStory1.url,
            imageUrl = nonHeadlineStory1.imageUrl,
            publishedTime = nonHeadlineStory1.publishedTime,
            isHeadline = nonHeadlineStory1.isHeadline
        )

        dao.deleteHeadlineStories()

        assertThat(queries.findAllStories().executeAsList())
            .isEqualTo(listOf(nonHeadlineStory1.copy(id = 3)))
    }

    @Test
    fun `deleteNonHeadlineStories() deletes all existing non-headline stories`() = runBlockingTest {
        queries.insertStory(
            title = headlineStory1.title,
            source = headlineStory1.source,
            author = headlineStory1.author,
            description = headlineStory1.description,
            url = headlineStory1.url,
            imageUrl = headlineStory1.imageUrl,
            publishedTime = headlineStory1.publishedTime,
            isHeadline = headlineStory1.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory1.title,
            source = nonHeadlineStory1.source,
            author = nonHeadlineStory1.author,
            description = nonHeadlineStory1.description,
            url = nonHeadlineStory1.url,
            imageUrl = nonHeadlineStory1.imageUrl,
            publishedTime = nonHeadlineStory1.publishedTime,
            isHeadline = nonHeadlineStory1.isHeadline
        )
        queries.insertStory(
            title = nonHeadlineStory2.title,
            source = nonHeadlineStory2.source,
            author = nonHeadlineStory2.author,
            description = nonHeadlineStory2.description,
            url = nonHeadlineStory2.url,
            imageUrl = nonHeadlineStory2.imageUrl,
            publishedTime = nonHeadlineStory2.publishedTime,
            isHeadline = nonHeadlineStory2.isHeadline
        )

        dao.deleteNonHeadlineStories()

        assertThat(queries.findAllStories().executeAsList())
            .isEqualTo(listOf(headlineStory1.copy(id = 1)))
    }
}
