package io.github.reactivecircus.analytics

interface AnalyticsApi {

    fun setCurrentScreenName(screenName: String, screenClass: String)

    fun setEnableAnalytics(enable: Boolean)

    fun setUserId(userId: String?)

    fun setUserProperty(name: String, value: String)

    fun logEvent(name: String, params: Map<String, *>? = null)
}
