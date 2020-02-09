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

    private val story1 = StoryEntity.Impl(
        id = 1,
        title = "Article 1",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 1000L
    )

    private val story2 = StoryEntity.Impl(
        id = 2,
        title = "Article 2",
        author = "Yang",
        description = "Description...",
        url = "url",
        imageUrl = "image-url",
        publishedTime = 2000L
    )

    @Test
    fun allStories() = runBlockingTest {
        assertThat(dao.allStories())
            .emitsExactly(emptyList())

        queries.insertStory(
            title = story1.title,
            author = story1.author,
            description = story1.description,
            url = story1.url,
            imageUrl = story1.imageUrl,
            publishedTime = story1.publishedTime
        )
        queries.insertStory(
            title = story2.title,
            author = story2.author,
            description = story2.description,
            url = story2.url,
            imageUrl = story2.imageUrl,
            publishedTime = story2.publishedTime
        )

        assertThat(dao.allStories())
            .emitsExactly(
                listOf(story1, story2)
            )
    }

    @Test
    fun storyById() = runBlockingTest {
        queries.insertStory(
            title = story1.title,
            author = story1.author,
            description = story1.description,
            url = story1.url,
            imageUrl = story1.imageUrl,
            publishedTime = story1.publishedTime
        )

        assertThat(dao.storyById(2))
            .isNull()

        assertThat(dao.storyById(story1.id))
            .isEqualTo(story1)
    }

    @Test
    fun updateStories_noExistingItems() = runBlockingTest {
        dao.updateStories(
            listOf(story1, story2)
        )

        assertThat(queries.findAllStories().executeAsList())
            .isEqualTo(listOf(story1, story2))
    }

    @Test
    fun updateStories_existingItems() = runBlockingTest {
        queries.insertStory(
            title = story1.title,
            author = story1.author,
            description = story1.description,
            url = story1.url,
            imageUrl = story1.imageUrl,
            publishedTime = story1.publishedTime
        )

        dao.updateStories(
            listOf(story1.copy(description = "New description"), story2)
        )

        assertThat(queries.findAllStories().executeAsList())
            .isEqualTo(listOf(story1.copy(description = "New description"), story2))
    }

    @Test
    fun deleteAll() = runBlockingTest {
        queries.insertStory(
            title = story1.title,
            author = story1.author,
            description = story1.description,
            url = story1.url,
            imageUrl = story1.imageUrl,
            publishedTime = story1.publishedTime
        )
        queries.insertStory(
            title = story2.title,
            author = story2.author,
            description = story2.description,
            url = story2.url,
            imageUrl = story2.imageUrl,
            publishedTime = story2.publishedTime
        )

        dao.deleteAll()

        assertThat(queries.findAllStories().executeAsList())
            .isEqualTo(emptyList<StoryEntity>())
    }
}
