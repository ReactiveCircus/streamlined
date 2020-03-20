package io.github.reactivecircus.streamlined.data.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.store.ext.RefreshCriteria
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import kotlinx.coroutines.CoroutineScope
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class]
)
interface DataComponent {

    val storyRepository: StoryRepository

    val bookmarkRepository: BookmarkRepository

    val headlineStoryStore: HeadlineStoryStore

    val personalizedStoryStore: PersonalizedStoryStore

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance coroutineDispatcherProvider: CoroutineDispatcherProvider,
            @BindsInstance processLifetimeCoroutineScope: CoroutineScope,
            @BindsInstance refreshCriteria: RefreshCriteria,
            @BindsInstance newsApiService: NewsApiService,
            @BindsInstance databaseName: String?
        ): DataComponent
    }

    companion object {
        fun factory(): Factory = DaggerDataComponent.factory()
    }
}
