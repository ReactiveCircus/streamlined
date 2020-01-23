package io.github.reactivecircus.streamlined

import androidx.test.core.app.ApplicationProvider
import io.github.reactivecircus.streamlined.di.AppComponent
import io.github.reactivecircus.streamlined.di.IntegrationTestAppComponent
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent
import timber.log.Timber

class IntegrationTestApp : StreamlinedApp() {

    override val appComponent: AppComponent
        get() = IntegrationTestAppComponent.factory()
            .create(
                context = this,
                testingFrameworkComponent = TestingFrameworkComponent.factory()
                    .create(ApplicationProvider.getApplicationContext())
            )

    override fun onCreate() {
        // initialize Timber
        Timber.plant(DebugTree())

        // initialize analytics api (disable)
        appComponent.analyticsApi.setEnableAnalytics(false)
    }
}
