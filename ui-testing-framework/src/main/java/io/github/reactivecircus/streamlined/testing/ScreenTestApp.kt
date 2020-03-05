package io.github.reactivecircus.streamlined.testing

import android.app.Application
import coil.Coil.setDefaultImageLoader
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent
import timber.log.Timber

internal class ScreenTestApp : Application() {

    private val testingFrameworkComponent: TestingFrameworkComponent by lazy {
        TestingFrameworkComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        // initialize Timber
        Timber.plant(TestDebugTree())

        // initialize analytics api (disable)
        testingFrameworkComponent.analyticsApi.setEnableAnalytics(false)

        // set default image loader
        setDefaultImageLoader(testingFrameworkComponent.imageLoader)
    }
}
