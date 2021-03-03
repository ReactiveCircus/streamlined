package io.github.reactivecircus.streamlined.testing

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.espresso.intent.Intents
import dagger.hilt.android.testing.HiltAndroidRule
import io.github.reactivecircus.streamlined.testing.assumption.DataAssumptions
import io.github.reactivecircus.streamlined.testing.assumption.NetworkAssumptions
import javax.inject.Inject
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import radiography.Radiography
import radiography.ViewStateRenderers.DefaultsIncludingPii

abstract class BaseScreenTest {

    @Suppress("LeakingThis")
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var networkAssumptions: NetworkAssumptions

    @Inject
    lateinit var dataAssumptions: DataAssumptions

    @Before
    open fun setUp() {
        hiltRule.inject()
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
        networkAssumptions.assumeNetworkConnected()
    }

    inline fun <reified A : Activity> launchActivityScenario(
        intent: Intent? = null
    ): ActivityScenario<A> {
        return launchActivity<A>(intent).also {
            Espresso.onIdle()
        }
    }

    inline fun <reified F : Fragment> launchFragmentInTest(
        fragmentArgs: Bundle? = null
    ) {
        val startActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java,
            )
        )
        ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
            val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                checkNotNull(F::class.java.classLoader),
                F::class.java.name,
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager.commitNow {
                add(android.R.id.content, fragment, "")
                setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
            }
        }
        Espresso.onIdle()
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
