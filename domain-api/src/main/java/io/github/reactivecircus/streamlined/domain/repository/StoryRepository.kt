package io.github.reactivecircus.streamlined.domain.repository

import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface StoryRepository {

    fun streamHeadlineStories(): Flow<StoreResponse<List<Story>>>

    // TODO use custom query type
    fun streamPersonalizedStories(query: String): Flow<StoreResponse<List<Story>>>

    suspend fun fetchHeadlineStories(): List<Story>

    // TODO use custom query type
    suspend fun fetchPersonalizedStories(query: String): List<Story>

    suspend fun getStoryById(id: Long): Story?
}
