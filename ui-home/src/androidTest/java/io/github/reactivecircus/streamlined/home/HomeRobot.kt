package io.github.reactivecircus.streamlined.home

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.github.reactivecircus.streamlined.ui.util.timeAgo
import org.hamcrest.CoreMatchers.not
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.ScreenRobot
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.blueprint.testing.assertion.recyclerViewHasSize
import reactivecircus.blueprint.testing.assertion.snackBarDisplayed
import reactivecircus.blueprint.testing.assertion.textDisplayed
import reactivecircus.blueprint.testing.assertion.viewDisplayed
import reactivecircus.blueprint.testing.matcher.withRecyclerView
import reactivecircus.blueprint.testing.scrollToItemInRecyclerView
import io.github.reactivecircus.streamlined.ui.R as CommonUiResource

fun homeScreen(block: HomeRobot.() -> Unit) = HomeRobot().apply { block() }

class HomeRobot : ScreenRobot<HomeRobot.Actions, HomeRobot.Assertions>(Actions(), Assertions()) {

    class Actions : RobotActions {

        fun clickRetryButton() {
            clickView(R.id.retryButton)
        }

        fun swipeToRefresh() {
            swipeDownOnView(R.id.homeFeedsRecyclerView)
        }
    }

    class Assertions : RobotAssertions {

        fun homeFeedsDisplayed(feedItems: List<FeedItem>) {
            val recyclerViewId = R.id.homeFeedsRecyclerView
            val storyImageViewId = R.id.storyImageView
            val storySourceTextViewId = R.id.storySourceTextView
            val storyTitleTextViewId = R.id.storyTitleTextView
            val publishedTimeTextViewId = R.id.publishedTimeTextView
            val bookmarkButtonId = R.id.bookmarkButton
            val moreButtonId = R.id.moreButton
            val headerTitleTextViewId = R.id.titleTextView
            val readMoreHeadlinesTextViewId = R.id.readMoreHeadlinesTextView
            val noStoriesTextViewId = R.id.noStoriesTextView

            recyclerViewHasSize(recyclerViewId, feedItems.size)

            feedItems.forEachIndexed { index, feedItem ->
                // scroll to the item to make sure it's visible
                scrollToItemInRecyclerView(recyclerViewId, index)

                when (feedItem) {
                    is FeedItem.Header -> {
                        val expectedSectionTitle = when (feedItem.feedType) {
                            FeedType.TopHeadlines -> getApplicationContext<Context>()
                                .getString(R.string.feed_type_top_headlines)

                            FeedType.ForYou -> getApplicationContext<Context>()
                                .getString(R.string.feed_type_for_you)
                        }

                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, headerTitleTextViewId)
                        ).check(
                            matches(withText(expectedSectionTitle))
                        )
                    }
                    is FeedItem.Content -> {
                        // swipe up to make sure item is fully displayed
                        onView(
                            withRecyclerView(recyclerViewId).atPositionOnView(
                                index, storyTitleTextViewId
                            )
                        ).perform(swipeUp())

                        // photo
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, storyImageViewId)
                        ).run {
                            if (feedItem.story.imageUrl != null) {
                                check(matches(isDisplayed()))
                            } else {
                                check(matches(not(isDisplayed())))
                            }
                        }

                        // source
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, storySourceTextViewId)
                        ).check(
                            matches(withText(feedItem.story.source))
                        )

                        // title
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, storyTitleTextViewId)
                        ).check(
                            matches(withText(feedItem.story.title))
                        )

                        // published time
                        val expectedPublishedTime = feedItem.story.publishedTime
                            .timeAgo(PUBLISHED_TIME_DATE_PATTERN)
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, publishedTimeTextViewId)
                        ).check(
                            matches(withText(expectedPublishedTime))
                        )

                        // bookmark button
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, bookmarkButtonId)
                        ).check(
                            matches(isDisplayed())
                        )

                        // more button
                        onView(
                            withRecyclerView(recyclerViewId).atPositionOnView(index, moreButtonId)
                        ).check(
                            matches(isDisplayed())
                        )
                    }
                    FeedItem.TopHeadlinesFooter -> {
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, readMoreHeadlinesTextViewId)
                        ).check(
                            matches(isDisplayed())
                        )
                    }
                    is FeedItem.Empty -> {
                        onView(
                            withRecyclerView(recyclerViewId)
                                .atPositionOnView(index, noStoriesTextViewId)
                        ).check(
                            matches(isDisplayed())
                        )
                    }
                }
            }
        }

        fun couldNotLoadContentErrorMessageDisplayed() {
            textDisplayed(CommonUiResource.string.error_message_could_not_load_content)
        }

        fun retryButtonDisplayed() {
            viewDisplayed(R.id.retryButton)
        }

        fun couldNotRefreshContentSnackbarDisplayed() {
            val errorMessage =
                getApplicationContext<Context>().resources.getString(
                    CommonUiResource.string.error_message_could_not_refresh_content
                )
            snackBarDisplayed(errorMessage)
        }
    }
}
