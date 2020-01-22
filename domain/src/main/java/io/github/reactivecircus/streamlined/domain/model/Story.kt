package io.github.reactivecircus.streamlined.domain.model

class Story(
    val id: Long,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedTime: Long
)
