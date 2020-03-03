package io.github.reactivecircus.streamlined.persistence

import kotlinx.coroutines.flow.Flow

interface StoryDao {

    fun allStories(): Flow<List<StoryEntity>>

    fun headlineStories(): Flow<List<StoryEntity>>

    fun nonHeadlineStories(): Flow<List<StoryEntity>>

    fun storyById(id: Long): StoryEntity?

    suspend fun updateStories(forHeadlines: Boolean, stories: List<StoryEntity>)

    suspend fun deleteAll()

    suspend fun deleteHeadlineStories()

    suspend fun deleteNonHeadlineStories()
}
