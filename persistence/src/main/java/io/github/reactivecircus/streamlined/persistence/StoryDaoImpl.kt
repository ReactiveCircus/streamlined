package io.github.reactivecircus.streamlined.persistence

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class StoryDaoImpl @Inject constructor(
    private val queries: StoryEntityQueries
) : StoryDao {
    override fun allStories(): Flow<List<StoryEntity>> {
        return queries.findAllStories().asFlow().mapToList()
    }

    override fun storyById(id: Long): StoryEntity? {
        return queries.findStoryById(id).executeAsOneOrNull()
    }

    override suspend fun insertStories(stories: List<StoryEntity>) {
        queries.transaction {
            stories.forEach {
                val storyExists = queries.findStoryIdByTitleAndPublishedTime(
                    title = it.title,
                    publishedTime = it.publishedTime
                ).executeAsOneOrNull() != null

                if (!storyExists) {
                    queries.insertStory(
                        title = it.title,
                        author = it.author,
                        description = it.description,
                        url = it.url,
                        imageUrl = it.imageUrl,
                        publishedTime = it.publishedTime
                    )
                }
            }
        }
    }

    override suspend fun deleteAll() {
        return queries.deleteAll()
    }
}
