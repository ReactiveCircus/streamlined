package io.github.reactivecircus.streamlined.home

import io.github.reactivecircus.streamlined.domain.model.Story

sealed class FeedItem {
    data class Header(val feedType: FeedType) : FeedItem()
    data class Content(val feedType: FeedType, val story: Story) : FeedItem()
    object TopHeadlinesFooter : FeedItem()
    data class Empty(val feedType: FeedType) : FeedItem()
}
