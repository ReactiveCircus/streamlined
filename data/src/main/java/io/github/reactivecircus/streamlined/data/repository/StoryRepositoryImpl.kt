package io.github.reactivecircus.streamlined.data.repository

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class StoryRepositoryImpl @Inject constructor(
    private val headlineStoryStore: HeadlineStoryStore,
    private val personalizedStoryStore: PersonalizedStoryStore,
    private val storyDao: StoryDao
) : StoryRepository {

    override fun streamHeadlineStories(refresh: Boolean): Flow<StoreResponse<List<Story>>> {
        return headlineStoryStore.stream(StoreRequest.cached(Unit, refresh))
    }

    override fun streamPersonalizedStories(
        query: String,
        refresh: Boolean
    ): Flow<StoreResponse<List<Story>>> {
        return personalizedStoryStore.stream(StoreRequest.cached(query, refresh))
    }

    override suspend fun fetchHeadlineStories(): List<Story> {
        return headlineStoryStore.fresh(Unit)
    }

    override suspend fun fetchPersonalizedStories(query: String): List<Story> {
        return personalizedStoryStore.fresh(query)
    }

    override suspend fun getStoryById(id: Long): Story? {
        return storyDao.storyById(id)?.toModel()
    }
}
