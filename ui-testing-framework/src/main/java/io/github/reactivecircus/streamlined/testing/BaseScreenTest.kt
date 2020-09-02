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
import io.github.reactivecircus.streamlined.testing.assumption.assumeNetworkConnected
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import radiography.Radiography
import radiography.ViewStateRenderers.DefaultsIncludingPii
import io.github.reactivecircus.streamlined.design.R as ThemeResource

abstract class BaseScreenTest {

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
        assumeNetworkConnected()
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
            themeResId = ThemeResource.style.Theme_Streamlined_DayNight,
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

        @Suppress("TooGenericExceptionCaught")
        override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
            try {
                delegate.handle(error, viewMatcher)
            } catch (decoratedError: Throwable) {
                val detailMessageField = Throwable::class.java.getDeclaredField("detailMessage")
                val previousAccessible = detailMessageField.isAccessible
                try {
                    detailMessageField.isAccessible = true
                    var message = (detailMessageField[decoratedError] as String?).orEmpty()
                    message = message.substringBefore("\nView Hierarchy:")
                    val prettyHierarchy = Radiography.scan(
                        viewStateRenderers = DefaultsIncludingPii
                    )
                    message += "\nView hierarchies:\n$prettyHierarchy"
                    detailMessageField[decoratedError] = message
                } finally {
                    detailMessageField.isAccessible = previousAccessible
                }
                throw decoratedError
            }
        }
    }
}
