package io.github.reactivecircus.analytics.noop

import android.app.Activity
import io.github.reactivecircus.analytics.AnalyticsApi

class NoOpAnalyticsApi : AnalyticsApi {

    override fun setCurrentScreenName(activity: Activity, name: String, className: String?) = Unit

    override fun setEnableAnalytics(enable: Boolean) = Unit

    override fun setUserId(userId: String?) = Unit

    override fun setUserProperty(name: String, value: String) = Unit

    override fun logEvent(name: String, params: Map<String, *>?) = Unit
}
