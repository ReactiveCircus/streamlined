package io.github.reactivecircus.streamlined.home

import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import io.github.reactivecircus.streamlined.testing.testHeadlineStories
import io.github.reactivecircus.streamlined.testing.testPersonalizedStories
import org.junit.Test

@LargeTest
@HiltAndroidTest
class HomeScreenTest : BaseScreenTest() {

    override fun setUp() {
        super.setUp()
        dataAssumptions.assumeNoCachedHeadlineStories()
        dataAssumptions.assumeNoCachedPersonalizedStories()
    }

    @Test
    fun launchHomeScreen_homeFeedsDisplayed() {
        homeScreen {
            perform {
                launchFragmentInTest<HomeFragment>()
            }
            check {
                homeFeedsDisplayed(testFeedItems)
            }
        }
    }

    @Test
    fun launchHomeScreenWithoutConnectivityOrCachedStories_errorMessageAndRetryButtonDisplayed() {
        homeScreen {
            given {
                networkAssumptions.assumeNetworkDisconnected()
            }
            perform {
                launchFragmentInTest<HomeFragment>()
            }
            check {
                couldNotLoadContentErrorMessageDisplayed()
                retryButtonDisplayed()
            }
        }
    }

    @Test
    fun clickRetryButtonWithConnectivity_homeFeedsDisplayed() {
        homeScreen {
            given {
                networkAssumptions.assumeNetworkDisconnected()
            }
            perform {
                launchFragmentInTest<HomeFragment>()
            }
            check {
                retryButtonDisplayed()
            }
            given {
                networkAssumptions.assumeNetworkConnected()
            }
            perform {
                clickRetryButton()
            }
            check {
                homeFeedsDisplayed(testFeedItems)
            }
        }
    }

    @Test
    fun clickRetryButtonWithoutConnectivity_errorMessageAndRetryButtonDisplayed() {
        homeScreen {
            given {
                networkAssumptions.assumeNetworkDisconnected()
            }
            perform {
                launchFragmentInTest<HomeFragment>()
                clickRetryButton()
            }
            check {
                couldNotLoadContentErrorMessageDisplayed()
                retryButtonDisplayed()
            }
        }
    }

    @Test
    fun launchHomeScreenWithCachedStoriesButNoConnectivity_cachedHomeFeedsAndErrorSnackbarDisplayed() {
        homeScreen {
            given {
                dataAssumptions.populateHeadlineStories()
                dataAssumptions.populatePersonalizedStories()
                networkAssumptions.assumeNetworkDisconnected()
            }
            perform {
                launchFragmentInTest<HomeFragment>()
            }
            check {
                contentDisplayed()
                couldNotRefreshContentSnackbarDisplayed()
                homeFeedsDisplayed(testFeedItems)
            }
        }
    }

    @Test
    fun swipeToRefreshHomeFeedsWithConnectivity_homeFeedsDisplayed() {
        homeScreen {
            perform {
                launchFragmentInTest<HomeFragment>()
                swipeToRefresh()
            }
            check {
                homeFeedsDisplayed(testFeedItems)
            }
        }
    }

    @Test
    fun swipeToRefreshHomeFeedsWithoutConnectivity_errorSnackbarDisplayed() {
        homeScreen {
            perform {
                launchFragmentInTest<HomeFragment>()
            }
            check {
                contentDisplayed()
            }
            given {
                networkAssumptions.assumeNetworkDisconnected()
            }
            perform {
                swipeToRefresh()
            }
            check {
                contentDisplayed()
                couldNotRefreshContentSnackbarDisplayed()
            }
        }
    }
}

private val testFeedItems = generateFeedItems(
    maxNumberOfHeadlines = DefaultHomeUiConfigs.NUMBER_OF_HEADLINES_DISPLAYED,
    headlineStories = testHeadlineStories,
    personalizedStories = testPersonalizedStories
)
