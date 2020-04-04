package io.github.reactivecircus.streamlined.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.analytics.noop.NoOpAnalyticsApi

@Module
object SdkModule {

    @Provides
    @Reusable
    fun provideAnalyticsApi(): AnalyticsApi {
        return NoOpAnalyticsApi
    }
}
