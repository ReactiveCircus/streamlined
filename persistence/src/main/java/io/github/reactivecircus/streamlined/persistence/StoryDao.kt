package io.github.reactivecircus.streamlined.persistence

import kotlinx.coroutines.flow.Flow

interface StoryDao {

    fun allStories(): Flow<List<StoryEntity>>

    fun storyById(id: Long): StoryEntity?

    suspend fun insertStories(stories: List<StoryEntity>)

    suspend fun deleteAll()
}
