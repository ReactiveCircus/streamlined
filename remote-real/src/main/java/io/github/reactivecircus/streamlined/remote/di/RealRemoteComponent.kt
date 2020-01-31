package io.github.reactivecircus.streamlined.remote.di

import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RealRemoteModule::class]
)
interface RealRemoteComponent {

    val newsApiService: NewsApiService

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance @BaseUrl baseUrl: String,
            @BindsInstance @ApiKey apiKey: String,
            @BindsInstance @NetworkTimeoutSeconds networkTimeoutSeconds: Long
        ): RealRemoteComponent
    }

    companion object {
        fun factory(): Factory = DaggerRealRemoteComponent.factory()
    }
}
