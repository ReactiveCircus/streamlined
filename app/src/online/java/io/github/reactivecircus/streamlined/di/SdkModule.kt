package io.github.reactivecircus.streamlined.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.analytics.firebase.FirebaseAnalyticsApi

@Module
@InstallIn(SingletonComponent::class)
object SdkModule {

    @Provides
    @Reusable
    fun analyticsApi(): AnalyticsApi {
        return FirebaseAnalyticsApi
    }
}
