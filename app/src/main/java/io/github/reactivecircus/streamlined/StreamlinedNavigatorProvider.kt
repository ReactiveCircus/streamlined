package io.github.reactivecircus.streamlined

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import javax.inject.Inject

/**
 * Implementation of [NavigatorProvider] which provides [Navigator] instances
 * bounded by the activity lifecycle.
 */
class StreamlinedNavigatorProvider @Inject constructor() :
    NavigatorProvider, Application.ActivityLifecycleCallbacks {

    private var navigator: Navigator? = null

    override fun get(): Navigator {
        return checkNotNull(navigator)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        navigator = StreamlinedNavigator { activity }
    }

    override fun onActivityDestroyed(activity: Activity) {
        navigator = null
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}
