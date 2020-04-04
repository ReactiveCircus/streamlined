package io.github.reactivecircus.analytics.firebase

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import io.github.reactivecircus.analytics.AnalyticsApi

object FirebaseAnalyticsApi : AnalyticsApi {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    /**
     * By default Firebase uses the class name of the activity for automatic screen reporting.
     * Call this in Activity.onResume() to set a custom name for the current screen.
     * Also call this to define new screens within an Activity triggered by changing
     * Fragments, navigating in ViewPager, etc.
     */
    override fun setCurrentScreenName(activity: Activity, name: String, className: String?) {
        firebaseAnalytics.setCurrentScreen(activity, name, className)
    }

    override fun setEnableAnalytics(enable: Boolean) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(enable)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }

    override fun logEvent(name: String, params: Map<String, *>?) {
        firebaseAnalytics.logEvent(name) {
            params?.entries?.run {
                forEach { entry ->
                    when (entry.value) {
                        is String -> param(entry.key, entry.value as String)
                        is Long -> param(entry.key, (entry.value as Long))
                        is Double -> param(entry.key, (entry.value as Double))
                    }
                }
            }
        }
    }
}
