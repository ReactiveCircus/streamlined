package io.github.reactivecircus.streamlined.home

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.home.di.HomeTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import io.github.reactivecircus.streamlined.testing.assumption.assumeNetworkConnected
import io.github.reactivecircus.streamlined.testing.assumption.assumeNetworkDisconnected
import io.github.reactivecircus.streamlined.testing.assumption.assumeNoCachedHeadlineStories
import io.github.reactivecircus.streamlined.testing.assumption.assumeNoCachedPersonalizedStories
import io.github.reactivecircus.streamlined.testing.assumption.populateHeadlineStories
import io.github.reactivecircus.streamlined.testing.assumption.populatePersonalizedStories
import io.github.reactivecircus.streamlined.testing.testHeadlineStories
import io.github.reactivecircus.streamlined.testing.testPersonalizedStories
import org.junit.Test
import kotlin.time.ExperimentalTime

@LargeTest
class HomeScreenTest : BaseScreenTest() {

    private val fragmentFactory = HomeTestAppComponent.getOrCreate().fragmentFactory

    override fun setUp() {
        super.setUp()
        assumeNoCachedHeadlineStories()
        assumeNoCachedPersonalizedStories()
    }

    @Test
    fun launchHomeScreen_homeFeedsDisplayed() {
        homeScreen {
            perform {
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
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
                assumeNetworkDisconnected()
            }
            perform {
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
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
                assumeNetworkDisconnected()
            }
            perform {
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
            }
            given {
                assumeNetworkConnected()
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
                assumeNetworkDisconnected()
            }
            perform {
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
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
                populateHeadlineStories()
                populatePersonalizedStories()
                assumeNetworkDisconnected()
            }
            perform {
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
            }
            check {
                couldNotRefreshContentSnackbarDisplayed()
                homeFeedsDisplayed(testFeedItems)
            }
        }
    }

    @Test
    fun swipeToRefreshHomeFeedsWithConnectivity_homeFeedsDisplayed() {
        homeScreen {
            perform {
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
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
                launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
            }
            given {
                assumeNetworkDisconnected()
            }
            perform {
                swipeToRefresh()
            }
            check {
                couldNotRefreshContentSnackbarDisplayed()
                homeFeedsDisplayed(testFeedItems)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
private val testFeedItems = generateFeedItems(
    maxNumberOfHeadlines = DefaultHomeUiConfigs.NUMBER_OF_HEADLINES_DISPLAYED,
    headlineStories = testHeadlineStories,
    personalizedStories = testPersonalizedStories
)
