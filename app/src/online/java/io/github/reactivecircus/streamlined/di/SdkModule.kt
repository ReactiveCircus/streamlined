package io.github.reactivecircus.streamlined.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.analytics.firebase.FirebaseAnalyticsApi

@Module
object SdkModule {

    @Provides
    @Reusable
    fun provideAnalyticsApi(application: Application): AnalyticsApi {
        return FirebaseAnalyticsApi(application)
    }
}
