package io.github.reactivecircus.streamlined.data.mapper

import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO

internal fun StoryDTO.toEntity(): StoryEntity {
    return StoryEntity.Impl(
        id = -1,
        title = title,
        author = author,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedAt = 0L // TODO add publishedAt.toTimestamp() extension in remote-base
    )
}

internal fun StoryEntity.toModel(): Story {
    return Story(
        id = id,
        title = title,
        author = author,
        description = description,
        url = url,
        imageUrl = imageUrl,
        publishedAt = publishedAt
    )
}
