package io.github.reactivecircus.streamlined.data.mapper

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.remote.dto.SourceDTO
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO
import org.junit.Test

class StoryMappersTest {

    private val isHeadline = true

    @Test
    fun `StoryDTO to entity()`() {
        val storyDTO = StoryDTO(
            source = SourceDTO("source"),
            author = "author",
            title = "Article",
            description = "Description...",
            url = "url",
            urlToImage = "image-url",
            publishedAt = "2020-02-07T22:37:23Z"
        )

        val expected = StoryEntity(
            id = -1,
            source = "source",
            title = "Article",
            author = "author",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1581115043000L,
            isHeadline = isHeadline
        )

        assertThat(storyDTO.toEntity(isHeadline))
            .isEqualTo(expected)
    }

    @Test
    fun `StoryEntity to model()`() {
        val storyEntity = StoryEntity(
            id = 1,
            source = "source",
            title = "Article",
            author = "author",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1234L,
            isHeadline = isHeadline
        )

        val expected = Story(
            id = 1,
            source = "source",
            title = "Article",
            author = "author",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1234L
        )

        assertThat(storyEntity.toModel())
            .isEqualTo(expected)
    }
}
