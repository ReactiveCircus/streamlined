package io.github.reactivecircus.streamlined.robot

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import io.github.reactivecircus.streamlined.R
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.ScreenRobot
import reactivecircus.blueprint.testing.action.selectNavigationBarItem
import reactivecircus.blueprint.testing.assertion.navigationBarViewItemSelected
import reactivecircus.blueprint.testing.assertion.toolbarHasTitle
import io.github.reactivecircus.streamlined.headlines.R as HeadlinesResource
import io.github.reactivecircus.streamlined.home.R as HomeResource
import io.github.reactivecircus.streamlined.navigator.R as NavigatorResource
import io.github.reactivecircus.streamlined.readinglist.R as ReadingListResource
import io.github.reactivecircus.streamlined.settings.R as SettingsResource

fun appScreen(block: AppRobot.() -> Unit) = AppRobot().apply { block() }

class AppRobot : ScreenRobot<AppRobot.Actions, AppRobot.Assertions>(Actions(), Assertions()) {

    class Actions : RobotActions {

        fun selectHomeNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_home)
            selectNavigationBarItem(R.id.bottomNavigationView, navItemTitle)
        }

        fun selectHeadlinesNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_headlines)
            selectNavigationBarItem(R.id.bottomNavigationView, navItemTitle)
        }

        fun selectReadingListNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_reading_list)
            selectNavigationBarItem(R.id.bottomNavigationView, navItemTitle)
        }

        fun selectSettingsNavItem() {
            val navItemTitle = getApplicationContext<Context>()
                .resources.getString(R.string.menu_title_settings)
            selectNavigationBarItem(R.id.bottomNavigationView, navItemTitle)
        }
    }

    class Assertions : RobotAssertions {

        fun homeScreenDisplayed() {
            toolbarHasTitle(HomeResource.string.title_home)
            navigationBarViewItemSelected(
                R.id.bottomNavigationView,
                NavigatorResource.id.homeScreen
            )
        }

        fun headlinesScreenDisplayed() {
            toolbarHasTitle(HeadlinesResource.string.title_headlines)
            navigationBarViewItemSelected(
                R.id.bottomNavigationView,
                NavigatorResource.id.headlinesScreen
            )
        }

        fun readingListScreenDisplayed() {
            toolbarHasTitle(ReadingListResource.string.title_reading_list)
            navigationBarViewItemSelected(
                R.id.bottomNavigationView,
                NavigatorResource.id.readingListScreen
            )
        }

        fun settingsScreenDisplayedSelected() {
            toolbarHasTitle(SettingsResource.string.title_settings)
            navigationBarViewItemSelected(
                R.id.bottomNavigationView,
                NavigatorResource.id.settingsScreen
            )
        }
    }
}
