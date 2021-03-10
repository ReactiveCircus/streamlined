package io.github.reactivecircus.streamlined.ui.testing

import io.github.reactivecircus.streamlined.data.mapper.toTimestamp
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.remote.MockData
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO

val testHeadlineStories = MockData.mockHeadlineStories.map { it.toModel() }
val testPersonalizedStories = MockData.mockPersonalizedStories.map { it.toModel() }

private fun StoryDTO.toModel(): Story {
    return Story(
        id = -1,
        source = source.name,
        title = title,
        author = author,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedTime = publishedAt.toTimestamp()
    )
}
