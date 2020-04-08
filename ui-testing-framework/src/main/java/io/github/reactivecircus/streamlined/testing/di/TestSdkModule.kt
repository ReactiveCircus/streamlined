package io.github.reactivecircus.streamlined.testing.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.analytics.noop.NoOpAnalyticsApi

@Module
internal object TestSdkModule {

    @Provides
    @Reusable
    fun analyticsApi(): AnalyticsApi {
        return NoOpAnalyticsApi
    }
}
