package io.github.reactivecircus.streamlined.testing

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.espresso.intent.Intents
import androidx.test.runner.screenshot.Screenshot
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import io.github.reactivecircus.streamlined.ui.R as ResourcesR

abstract class BaseScreenTest {

    protected val testingFrameworkComponent = TestingFrameworkComponent.factory()
        .create(ApplicationProvider.getApplicationContext())

    private val networkAssumptions = testingFrameworkComponent.networkAssumptions

    @Before
    open fun setUp() {
        Intents.init()

        // set up global failure handler
        Espresso.setFailureHandler(
            GlobalFailureHandler(
                ApplicationProvider.getApplicationContext()
            )
        )
    }

    @After
    open fun tearDown() {
        Intents.release()
        // reset network connectivity
        networkAssumptions.assumeNetworkConnected()
    }

    inline fun <reified A : Activity> launchActivityScenario(
        intent: android.content.Intent? = null
    ): ActivityScenario<A> {
        return launchActivity<A>(intent).also {
            Espresso.onIdle()
        }
    }

    inline fun <reified F : Fragment> launchFragmentScenario(
        factory: FragmentFactory,
        fragmentArgs: Bundle? = null
    ): FragmentScenario<F> {
        return launchFragmentInContainer<F>(
            fragmentArgs = fragmentArgs,
            themeResId = ResourcesR.style.Theme_Streamlined_DayNight,
            factory = factory
        ).also {
            Espresso.onIdle()
        }
    }

    private class GlobalFailureHandler(targetContext: Context) : FailureHandler {

        private val delegate: FailureHandler

        init {
            delegate = DefaultFailureHandler(targetContext)
        }

        override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
            Screenshot.capture()
            delegate.handle(error, viewMatcher)
        }
    }
}
