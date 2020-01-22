package io.github.reactivecircus.streamlined.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import io.github.reactivecircus.streamlined.remote.di.MockRemoteComponent

@Module
object ServiceModule {

    @Provides
    @Reusable
    fun provideNewsApiService(): NewsApiService {
        return MockRemoteComponent.factory()
            .create()
            .newsApiService
    }
}
