package io.github.reactivecircus.streamlined.testing.di

import android.content.Context
import coil.ImageLoader
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.testing.assumption.NetworkAssumptions
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestAppModule::class,
        TestServiceModule::class,
        TestRepositoryModule::class,
        TestSdkModule::class
    ]
)
interface TestingFrameworkComponent {

    val imageLoader: ImageLoader

    val animationConfigs: AnimationConfigs

    val coroutineDispatcherProvider: CoroutineDispatcherProvider

    val analyticsApi: AnalyticsApi

    val networkAssumptions: NetworkAssumptions

    val storyRepository: StoryRepository

    val bookmarkRepository: BookmarkRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestingFrameworkComponent
    }

    companion object {
        fun factory(): Factory = DaggerTestingFrameworkComponent.factory()
    }
}
