package io.github.reactivecircus.streamlined.remote.dto

import kotlinx.serialization.Serializable

@Serializable
class StoryDTO(
    val source: SourceDTO,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String
)

@Serializable
class SourceDTO(val name: String)
