package io.github.reactivecircus.streamlined.data.repository

import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import io.github.reactivecircus.store.ext.RefreshPolicy
import io.github.reactivecircus.store.ext.streamWithRefreshPolicy
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
internal class StoryRepositoryImpl @Inject constructor(
    private val headlineStoryStore: HeadlineStoryStore,
    private val personalizedStoryStore: PersonalizedStoryStore,
    private val refreshPolicy: RefreshPolicy,
    private val storyDao: StoryDao
) : StoryRepository {

    override fun streamHeadlineStories(): Flow<StoreResponse<List<Story>>> {
        return headlineStoryStore.streamWithRefreshPolicy(Unit, refreshPolicy)
    }

    override fun streamPersonalizedStories(query: String): Flow<StoreResponse<List<Story>>> {
        return personalizedStoryStore.streamWithRefreshPolicy(query, refreshPolicy)
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
