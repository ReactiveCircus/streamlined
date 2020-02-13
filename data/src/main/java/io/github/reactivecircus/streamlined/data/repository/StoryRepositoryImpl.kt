package io.github.reactivecircus.streamlined.data.repository

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class StoryRepositoryImpl @Inject constructor(
    private val headlineStoryStore: Store<Unit, List<Story>>,
    private val personalizedStoryStore: Store<String, List<Story>>,
    private val storyDao: StoryDao
) : StoryRepository {

    override fun streamHeadlineStories(): Flow<StoreResponse<List<Story>>> {
        return headlineStoryStore.stream(StoreRequest.cached(Unit, refresh = true))
    }

    override fun streamPersonalizedStories(query: String): Flow<StoreResponse<List<Story>>> {
        return personalizedStoryStore.stream(StoreRequest.cached(query, refresh = true))
    }

    override suspend fun getStoryById(id: Long): Story? {
        return storyDao.storyById(id)?.toModel()
    }
}
