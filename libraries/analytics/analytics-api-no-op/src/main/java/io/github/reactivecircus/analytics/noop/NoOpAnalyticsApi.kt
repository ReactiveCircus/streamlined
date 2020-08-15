package io.github.reactivecircus.analytics.noop

import io.github.reactivecircus.analytics.AnalyticsApi

object NoOpAnalyticsApi : AnalyticsApi {

    override fun setCurrentScreenName(screenName: String, screenClass: String) = Unit

    override fun setEnableAnalytics(enable: Boolean) = Unit

    override fun setUserId(userId: String?) = Unit

    override fun setUserProperty(name: String, value: String) = Unit

    override fun logEvent(name: String, params: Map<String, *>?) = Unit
}
