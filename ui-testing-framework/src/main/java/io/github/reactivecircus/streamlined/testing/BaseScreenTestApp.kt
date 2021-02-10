package io.github.reactivecircus.streamlined.testing

import android.app.Application
import coil.Coil.setImageLoader
import coil.ImageLoader
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

abstract class BaseScreenTestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Timber
        Timber.plant(TestDebugTree())

        // set default image loader
        setImageLoader(
            EntryPointAccessors.fromApplication(this, ImageLoaderEntryPoint::class.java)
                .imageLoader()
        )
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ImageLoaderEntryPoint {
        fun imageLoader(): ImageLoader
    }
}
