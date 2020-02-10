package io.github.reactivecircus.streamlined.data.mapper

import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO
import io.github.reactivecircus.streamlined.remote.helper.toTimestamp

internal fun StoryDTO.toEntity(): StoryEntity {
    return StoryEntity.Impl(
        id = -1,
        title = title,
        author = author,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedTime = publishedAt.toTimestamp()
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
        publishedTime = publishedTime
    )
}
