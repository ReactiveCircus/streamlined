package io.github.reactivecircus.streamlined.persistence

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class StoryDaoImpl @Inject constructor(
    private val queries: StoryEntityQueries,
    private val databaseConfigs: DatabaseConfigs,
) : StoryDao {
    override fun allStories(): Flow<List<StoryEntity>> {
        return queries.findAllStories().asFlow().mapToList(databaseConfigs.coroutineContext)
    }

    override fun headlineStories(): Flow<List<StoryEntity>> {
        return queries.findStories(isHeadline = true).asFlow()
            .mapToList(databaseConfigs.coroutineContext)
    }

    override fun nonHeadlineStories(): Flow<List<StoryEntity>> {
        return queries.findStories(isHeadline = false).asFlow()
            .mapToList(databaseConfigs.coroutineContext)
    }

    override fun storyById(id: Long): StoryEntity? {
        return queries.findStoryById(id).executeAsOneOrNull()
    }

    override suspend fun updateStories(forHeadlines: Boolean, stories: List<StoryEntity>) {
        queries.transaction {
            val ids = queries.findStoryIds(isHeadline = forHeadlines).executeAsList().toMutableSet()

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
                        source = it.source,
                        author = it.author,
                        description = it.description,
                        url = it.url,
                        imageUrl = it.imageUrl,
                        publishedTime = it.publishedTime,
                        isHeadline = forHeadlines
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
        queries.deleteAll()
    }

    override suspend fun deleteHeadlineStories() {
        queries.deleteHeadlineStories()
    }

    override suspend fun deleteNonHeadlineStories() {
        queries.deleteNonHeadlineStories()
    }
}
