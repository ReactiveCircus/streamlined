package io.github.reactivecircus.streamlined.data.mapper

import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO

internal fun StoryDTO.toEntity(isHeadline: Boolean): StoryEntity {
    return StoryEntity.Impl(
        id = -1,
        source = source.name,
        title = title,
        author = author,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedTime = publishedAt.toTimestamp(),
        isHeadline = isHeadline
    )
}

internal fun StoryEntity.toModel(): Story {
    return Story(
        id = id,
        source = source,
        title = title,
        author = author,
        description = description,
        url = url,
        imageUrl = imageUrl,
        publishedTime = publishedTime
    )
}
