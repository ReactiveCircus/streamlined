package io.github.reactivecircus.streamlined.domain.model

data class Story(
    val id: Long,
    val source: String,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String?,
    val publishedTime: Long
)
