package io.github.reactivecircus.streamlined

import android.annotation.SuppressLint
import android.app.Application
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Client
import io.github.reactivecircus.analytics.AnalyticsApi
import reactivecircus.bugsnag.BugsnagTree
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("Registered")
open class StreamlinedApp : Application() {

    private lateinit var bugsnagClient: Client

    @Inject
    lateinit var analyticsApi: AnalyticsApi

    override fun onCreate() {
        super.onCreate()

        // TODO build app graph and inject

        // initialize Bugsnag
        if (BuildConfig.ENABLE_BUGSNAG) {
            bugsnagClient = Bugsnag.init(this).apply {
                setReleaseStage(BuildConfig.BUILD_TYPE)
                config.detectAnrs = false
                config.detectNdkCrashes = false
            }
        }

        // initialize Timber
        initializeTimber()

        // initialize analytics api
        // TODO analyticsApi.setEnableAnalytics(BuildConfig.ENABLE_ANALYTICS)
    }

    protected open fun initializeTimber() {
        val tree = BugsnagTree(bugsnagClient)
        Timber.plant(tree)
        bugsnagClient.beforeNotify { error ->
            tree.update(error)
            return@beforeNotify true
        }
    }
}
