package io.github.reactivecircus.streamlined.persistence

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class StoryDaoImpl @Inject constructor(
    private val queries: StoryEntityQueries
) : StoryDao {
    override fun allStories(): Flow<List<StoryEntity>> {
        return queries.allStories().asFlow().mapToList()
    }

    override suspend fun updateStories(stories: List<StoryEntity>) {
        TODO("not implemented")
    }

    override suspend fun deleteAll() {
        return queries.deleteAll()
    }
}
