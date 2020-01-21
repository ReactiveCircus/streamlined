package io.github.reactivecircus.streamlined.remote.di

import dagger.Component
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MockRemoteModule::class]
)
interface MockRemoteComponent {

    val newsApiService: NewsApiService

    @Component.Factory
    interface Factory {

        fun create(): MockRemoteComponent
    }

    companion object {
        fun factory(): Factory = DaggerMockRemoteComponent.factory()
    }
}
