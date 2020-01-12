package io.github.reactivecircus.analytics

import android.app.Activity

interface AnalyticsApi {

    fun setCurrentScreenName(activity: Activity, name: String, className: String? = null)

    fun setEnableAnalytics(enable: Boolean)

    fun setUserId(userId: String?)

    fun setUserProperty(name: String, value: String)

    fun logEvent(name: String, params: Map<String, *>? = null)
}
