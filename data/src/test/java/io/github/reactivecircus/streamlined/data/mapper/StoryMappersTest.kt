package io.github.reactivecircus.streamlined.data.mapper

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.remote.dto.SourceDTO
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO
import org.junit.Test

class StoryMappersTest {

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

        val expected = StoryEntity.Impl(
            id = -1,
            title = "Article",
            author = "author",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1581115043000L
        )

        assertThat(storyDTO.toEntity())
            .isEqualTo(expected)
    }

    @Test
    fun `StoryEntity to model()`() {
        val storyEntity = StoryEntity.Impl(
            id = 1,
            title = "Article",
            author = "author",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1234L
        )

        val expected = Story(
            id = 1,
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
