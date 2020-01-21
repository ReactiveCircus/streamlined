package io.github.reactivecircus.streamlined.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StoryDTO(
    val source: SourceDTO,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String
)
