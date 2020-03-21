package io.github.reactivecircus.streamlined.data.testutil

import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TestStoryDao(
    private val expectedAllStoriesFlow: Flow<List<StoryEntity>> = emptyFlow(),
    private val expectedHeadlineStoriesFlow: Flow<List<StoryEntity>> = emptyFlow(),
    private val expectedNonHeadlineStoriesFlow: Flow<List<StoryEntity>> = emptyFlow(),
    private val expectedStoryByIdResult: StoryEntity? = null,
    private val updateStoriesAction: () -> Unit = {},
    private val deleteAllAction: () -> Unit = {},
    private val deleteHeadlineStoriesAction: () -> Unit = {},
    private val deleteNonHeadlineStoriesAction: () -> Unit = {}
) : StoryDao {
    override fun allStories(): Flow<List<StoryEntity>> {
        return expectedAllStoriesFlow
    }

    override fun headlineStories(): Flow<List<StoryEntity>> {
        return expectedHeadlineStoriesFlow
    }

    override fun nonHeadlineStories(): Flow<List<StoryEntity>> {
        return expectedNonHeadlineStoriesFlow
    }

    override fun storyById(id: Long): StoryEntity? {
        return expectedStoryByIdResult
    }

    override suspend fun updateStories(forHeadlines: Boolean, stories: List<StoryEntity>) {
        updateStoriesAction.invoke()
    }

    override suspend fun deleteAll() {
        deleteAllAction.invoke()
    }

    override suspend fun deleteHeadlineStories() {
        deleteHeadlineStoriesAction.invoke()
    }

    override suspend fun deleteNonHeadlineStories() {
        deleteNonHeadlineStoriesAction.invoke()
    }
}
