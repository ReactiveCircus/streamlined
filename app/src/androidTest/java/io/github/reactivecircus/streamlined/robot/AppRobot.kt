package io.github.reactivecircus.streamlined.robot

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import io.github.reactivecircus.streamlined.R
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.ScreenRobot
import reactivecircus.blueprint.testing.action.selectBottomNavigationItem
import reactivecircus.blueprint.testing.assertion.bottomNavigationViewItemSelected
import reactivecircus.blueprint.testing.assertion.toolbarHasTitle

fun appScreen(block: AppRobot.() -> Unit) = AppRobot().apply { block() }

class AppRobot : ScreenRobot<AppRobot.Actions, AppRobot.Assertions>(Actions(), Assertions()) {
    val navHostViewId = R.id.mainNavHostFragment

    class Actions : RobotActions {

        fun selectHomeNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_home)
            selectBottomNavigationItem(R.id.bottomNavigationView, navItemTitle)
        }

        fun selectHeadlinesNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_headlines)
            selectBottomNavigationItem(R.id.bottomNavigationView, navItemTitle)
        }

        fun selectReadingListNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_reading_list)
            selectBottomNavigationItem(R.id.bottomNavigationView, navItemTitle)
        }

        fun selectSettingsNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_settings)
            selectBottomNavigationItem(R.id.bottomNavigationView, navItemTitle)
        }
    }

    class Assertions : RobotAssertions {

        fun homeScreenDisplayed() {
            toolbarHasTitle(R.string.title_home)
            bottomNavigationViewItemSelected(R.id.bottomNavigationView, R.id.homeFragment)
        }

        fun headlinesScreenDisplayed() {
            toolbarHasTitle(R.string.title_headlines)
            bottomNavigationViewItemSelected(R.id.bottomNavigationView, R.id.headlinesFragment)
        }

        fun readingListScreenDisplayed() {
            toolbarHasTitle(R.string.title_reading_list)
            bottomNavigationViewItemSelected(
                R.id.bottomNavigationView,
                R.id.readingListFragment
            )
        }

        fun settingsScreenDisplayedSelected() {
            toolbarHasTitle(R.string.title_settings)
            bottomNavigationViewItemSelected(R.id.bottomNavigationView, R.id.settingsFragment)
        }
    }
}
