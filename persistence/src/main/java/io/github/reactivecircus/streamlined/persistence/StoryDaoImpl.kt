package io.github.reactivecircus.streamlined.persistence

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal class StoryDaoImpl @Inject constructor(
    private val queries: StoryEntityQueries,
    private val context: CoroutineContext
) : StoryDao {
    override fun allStories(): Flow<List<StoryEntity>> {
        // TODO replace with allStoriesByFilter(...)
        return queries.findAllStories().asFlow().mapToList(context)
    }

    override fun storyById(id: Long): StoryEntity? {
        return queries.findStoryById(id).executeAsOneOrNull()
    }

    override suspend fun updateStories(stories: List<StoryEntity>) {
        queries.transaction {
            val ids = queries.findAllStoryIds().executeAsList().toMutableSet()

            stories.forEach {
                val existingStoryId = queries.findStoryIdByTitleAndPublishedTime(
                    title = it.title,
                    publishedTime = it.publishedTime
                ).executeAsOneOrNull()

                if (existingStoryId != null) {
                    ids -= existingStoryId
                    queries.updateStory(
                        title = it.title,
                        publishedTime = it.publishedTime,
                        author = it.author,
                        description = it.description,
                        url = it.url,
                        imageUrl = it.imageUrl
                    )
                } else {
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

            ids.chunked(MAX_PARAMETERS_PER_STATEMENT).forEach { chunk ->
                // TODO check if the story to be deleted is bookmarked.
                //  if bookmarked, remove all relevant records in StoryFilter but leave it in StoryEntity
                //  otherwise delete (cascade?) it (and relevant records in other tables e.g. StoryFilter)
                queries.deleteStoriesByIds(chunk)
            }
        }
    }

    override suspend fun deleteAll() {
        return queries.deleteAll()
    }
}
