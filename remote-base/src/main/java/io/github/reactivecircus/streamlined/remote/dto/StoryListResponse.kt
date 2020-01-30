package io.github.reactivecircus.streamlined.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StoryListResponse(
    val totalResults: Int,
    @SerialName("articles") val stories: List<StoryDTO>
)
