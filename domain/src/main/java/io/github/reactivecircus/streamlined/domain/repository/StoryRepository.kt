package io.github.reactivecircus.streamlined.domain.repository

import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface StoryRepository {

    fun streamStories(): Flow<StoreResponse<List<Story>>>

    suspend fun getStoryById(id: Long): Story?
}
