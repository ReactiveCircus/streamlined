package io.github.reactivecircus.streamlined.remote.api

import io.github.reactivecircus.streamlined.remote.dto.StoryListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun headlines(@Query("country") country: String): StoryListResponse

    @GET("everything")
    suspend fun everything(): StoryListResponse
}
