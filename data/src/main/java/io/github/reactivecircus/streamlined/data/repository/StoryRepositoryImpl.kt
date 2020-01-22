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

class StoryRepositoryImpl @Inject constructor(
    private val storyStore: Store<Unit, List<Story>>,
    private val storyDao: StoryDao
) : StoryRepository {

    override fun streamStories(): Flow<StoreResponse<List<Story>>> {
        return storyStore.stream(StoreRequest.cached(Unit, refresh = true))
    }

    override suspend fun getStoryById(id: Long): Story? {
        return storyDao.storyById(id)?.toModel()
    }
}
