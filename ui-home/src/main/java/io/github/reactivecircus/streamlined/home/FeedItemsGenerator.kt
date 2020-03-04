package io.github.reactivecircus.streamlined.home

import io.github.reactivecircus.streamlined.domain.model.Story

/**
 * Merges headline stories and personalized stories into a single list of [FeedItem]s,
 * adding the appropriate headers and footers.
 */
internal fun generateFeedItems(
    maxNumberOfHeadlines: Int,
    headlineStories: List<Story>,
    personalizedStories: List<Story>
): List<FeedItem> {
    return mutableListOf<FeedItem>().apply {
        // headline stories
        add(FeedItem.Header(FeedType.TopHeadlines))
        if (headlineStories.isNotEmpty()) {
            addAll(
                headlineStories.take(maxNumberOfHeadlines).map { story ->
                    FeedItem.Content(FeedType.TopHeadlines, story)
                }
            )
            add(FeedItem.TopHeadlinesFooter)
        } else {
            add(FeedItem.Empty(FeedType.TopHeadlines))
        }
        // personalized stories
        add(FeedItem.Header(FeedType.ForYou))
        if (personalizedStories.isNotEmpty()) {
            addAll(
                personalizedStories.map { story ->
                    FeedItem.Content(FeedType.ForYou, story)
                }
            )
        } else {
            add(FeedItem.Empty(FeedType.ForYou))
        }
    }
}
