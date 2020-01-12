package io.github.reactivecircus.streamlined

import android.os.StrictMode
import timber.log.Timber

class DebugStreamlinedApp : StreamlinedApp() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.ENABLE_STRICT_MODE) {
            StrictMode.enableDefaults()
        }
    }

    override fun initializeTimber() {
        if (BuildConfig.ENABLE_BUGSNAG) {
            super.initializeTimber()
        } else {
            Timber.plant(DebugTree())
        }
    }
}
