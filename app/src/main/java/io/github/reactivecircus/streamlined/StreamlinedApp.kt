package io.github.reactivecircus.streamlined

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.work.Configuration
import coil.Coil.setImageLoader
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Client
import io.github.reactivecircus.bugsnag.BugsnagTree
import io.github.reactivecircus.streamlined.di.AppComponent
import timber.log.Timber

@SuppressLint("Registered")
open class StreamlinedApp : Application(), Configuration.Provider {

    protected open val appComponent: AppComponent by lazy {
        AppComponent.factory().create(this)
    }

    private lateinit var bugsnagClient: Client

    override fun onCreate() {
        super.onCreate()

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
        appComponent.analyticsApi.setEnableAnalytics(BuildConfig.ENABLE_ANALYTICS)

        // schedule background sync
        appComponent.taskScheduler.scheduleHourlyStorySync()

        // register lifecycle hook for NavigatorProvider
        registerActivityLifecycleCallbacks(
            appComponent.navigatorProvider as StreamlinedNavigatorProvider
        )

        // set default image loader
        setImageLoader(appComponent.imageLoader)
    }

    protected open fun initializeTimber() {
        val tree = BugsnagTree(bugsnagClient)
        Timber.plant(tree)
        bugsnagClient.beforeNotify { error ->
            tree.update(error)
            return@beforeNotify true
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return appComponent.workManagerConfiguration
    }

    companion object {
        fun appComponent(context: Context) =
            (context.applicationContext as StreamlinedApp).appComponent
    }
}

val Activity.appComponent get() = StreamlinedApp.appComponent(this)
