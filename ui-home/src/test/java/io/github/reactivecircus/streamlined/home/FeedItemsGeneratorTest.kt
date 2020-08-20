package io.github.reactivecircus.streamlined.home

import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.model.Story
import org.junit.Test

class FeedItemsGeneratorTest {

    private val headlineStories = listOf(
        Story(
            id = 1,
            source = "source1",
            title = "Article 1",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1000L
        ),
        Story(
            id = 2,
            source = "source2",
            title = "Article 2",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 2000L
        ),
    )

    private val personalizedStories = listOf(
        Story(
            id = 3,
            source = "source3",
            title = "Article 3",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 3000L
        ),
        Story(
            id = 4,
            source = "source4",
            title = "Article 4",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 4000L
        ),
    )

    @Test
    fun `headline stories and personalized stories are combined into a list of feed items with headers and footers`() {
        val feedItems = generateFeedItems(
            maxNumberOfHeadlines = 5,
            headlineStories = headlineStories,
            personalizedStories = personalizedStories
        )
        assertThat(feedItems)
            .containsExactly(
                FeedItem.Header(FeedType.TopHeadlines),
                FeedItem.Content(FeedType.TopHeadlines, headlineStories[0]),
                FeedItem.Content(FeedType.TopHeadlines, headlineStories[1]),
                FeedItem.TopHeadlinesFooter,
                FeedItem.Header(FeedType.ForYou),
                FeedItem.Content(FeedType.ForYou, personalizedStories[0]),
                FeedItem.Content(FeedType.ForYou, personalizedStories[1])
            )
    }

    @Test
    fun `only the first n headline stories are present in the generated feed items where n is maxNumberOfHeadlines`() {
        val feedItems = generateFeedItems(
            maxNumberOfHeadlines = 1,
            headlineStories = headlineStories,
            personalizedStories = personalizedStories
        )
        assertThat(feedItems)
            .containsExactly(
                FeedItem.Header(FeedType.TopHeadlines),
                FeedItem.Content(FeedType.TopHeadlines, headlineStories[0]),
                FeedItem.TopHeadlinesFooter,
                FeedItem.Header(FeedType.ForYou),
                FeedItem.Content(FeedType.ForYou, personalizedStories[0]),
                FeedItem.Content(FeedType.ForYou, personalizedStories[1])
            )
    }

    @Test
    fun `FeedItem#Empty is added after header instead of footer when a stories list is empty`() {
        val feedItems = generateFeedItems(
            maxNumberOfHeadlines = 5,
            headlineStories = emptyList(),
            personalizedStories = emptyList()
        )
        assertThat(feedItems)
            .containsExactly(
                FeedItem.Header(FeedType.TopHeadlines),
                FeedItem.Empty(FeedType.TopHeadlines),
                FeedItem.Header(FeedType.ForYou),
                FeedItem.Empty(FeedType.ForYou)
            )
    }
}
