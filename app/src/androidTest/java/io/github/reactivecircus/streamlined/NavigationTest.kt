package io.github.reactivecircus.streamlined

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.robot.appScreen
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class NavigationTest : BaseScreenTest() {

    @Test
    fun selectNewBottomNavigationItem_correctDestinationSelected() {
        appScreen {
            perform {
                launchActivityScenario<StreamlinedActivity>()
            }
            check {
                homeScreenDisplayed()
            }
            perform {
                selectHeadlinesNavItem()
            }
            check {
                headlinesScreenDisplayed()
            }
            perform {
                selectReadingListNavItem()
            }
            check {
                readingListScreenDisplayed()
            }
            perform {
                selectSettingsNavItem()
            }
            check {
                settingsScreenDisplayedSelected()
            }
            perform {
                selectHomeNavItem()
            }
            check {
                homeScreenDisplayed()
            }
        }
    }
}
