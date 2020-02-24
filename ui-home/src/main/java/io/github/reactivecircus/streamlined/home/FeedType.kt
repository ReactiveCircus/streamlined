package io.github.reactivecircus.streamlined.home

sealed class FeedType {
    object TopHeadlines : FeedType()
    object ForYou : FeedType()
}
