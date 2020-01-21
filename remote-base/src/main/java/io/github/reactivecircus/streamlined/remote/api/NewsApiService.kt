package io.github.reactivecircus.streamlined.remote.api

import io.github.reactivecircus.streamlined.remote.dto.StoryDTO
import retrofit2.http.GET

interface NewsApiService {

    @GET("top-headlines")
    suspend fun headlines(): List<StoryDTO>

    @GET("everything")
    suspend fun everything(): List<StoryDTO>
}
