package io.github.reactivecircus.streamlined.data.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class]
)
interface DataComponent {

    val storyRepository: StoryRepository

    val bookmarkRepository: BookmarkRepository

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance coroutineDispatcherProvider: CoroutineDispatcherProvider,
            @BindsInstance newsApiService: NewsApiService,
            @BindsInstance databaseName: String?
        ): DataComponent
    }

    companion object {
        fun factory(): Factory = DaggerDataComponent.factory()
    }
}
