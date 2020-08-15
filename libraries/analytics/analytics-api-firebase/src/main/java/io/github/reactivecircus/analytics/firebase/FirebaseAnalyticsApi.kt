package io.github.reactivecircus.analytics.firebase

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import io.github.reactivecircus.analytics.AnalyticsApi

object FirebaseAnalyticsApi : AnalyticsApi {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun setCurrentScreenName(screenName: String, screenClass: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
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
