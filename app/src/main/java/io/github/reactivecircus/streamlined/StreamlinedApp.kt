package io.github.reactivecircus.streamlined

import android.annotation.SuppressLint
import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.Coil.setImageLoader
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.bugsnag.BugsnagTree
import io.github.reactivecircus.streamlined.work.scheduler.TaskScheduler
import javax.inject.Inject
import timber.log.Timber

@SuppressLint("Registered")
@HiltAndroidApp
open class StreamlinedApp : Application(), Configuration.Provider {

    @Inject
    lateinit var analyticsApi: AnalyticsApi

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var taskScheduler: TaskScheduler

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()

        // initialize Timber
        initializeTimber()

        // initialize analytics api
        analyticsApi.setEnableAnalytics(BuildConfig.ENABLE_ANALYTICS)

        // schedule background sync
        taskScheduler.scheduleHourlyStorySync()

        // set default image loader
        setImageLoader(imageLoader)
    }

    protected open fun initializeTimber() {
        val tree = BugsnagTree()
        Timber.plant(tree)

        // initialize Bugsnag
        if (BuildConfig.ENABLE_BUGSNAG) {
            initializeBugsnag(tree)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }
}
