package io.github.reactivecircus.streamlined.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.streamlined.BuildConfig
import io.github.reactivecircus.streamlined.remote.ApiConfigs

@Module
@InstallIn(SingletonComponent::class)
object EnvironmentModule {

    @Provides
    @Reusable
    fun apiConfigs(): ApiConfigs {
        return ApiConfigs(
            apiKey = BuildConfig.API_KEY,
            baseUrl = BuildConfig.BASE_URL,
        )
    }
}
