package io.github.reactivecircus.streamlined.persistence

import kotlinx.coroutines.flow.Flow

interface StoryDao {

    fun allStories(): Flow<List<StoryEntity>>

    fun storyById(id: Long): StoryEntity?

    suspend fun updateStories(stories: List<StoryEntity>)

    suspend fun deleteAll()
}
