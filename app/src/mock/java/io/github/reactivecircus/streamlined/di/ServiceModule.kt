package io.github.reactivecircus.streamlined.di

import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import io.github.reactivecircus.streamlined.remote.di.MockRemoteComponent

@Module
object ServiceModule {

    @Provides
    fun provideNewsApiService(): NewsApiService {
        return MockRemoteComponent.factory()
            .create()
            .newsApiService
    }
}
