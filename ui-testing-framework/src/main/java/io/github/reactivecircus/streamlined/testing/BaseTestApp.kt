package io.github.reactivecircus.streamlined.testing

import android.app.Application
import io.github.reactivecircus.analytics.AnalyticsApi
import timber.log.Timber
import javax.inject.Inject

abstract class BaseTestApp : Application() {

    @Inject
    lateinit var analyticsApi: AnalyticsApi

    override fun onCreate() {
        super.onCreate()

        // TODO build test graph and inject

        // initialize Timber
        Timber.plant(TestDebugTree())

        // initialize analytics api (disable)
        analyticsApi.setEnableAnalytics(false)
    }
}
