package io.github.reactivecircus.streamlined.testing.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import io.github.reactivecircus.streamlined.ui.di.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
internal object TestRepositoryModule {

    @Provides
    @Reusable
    fun provideDataComponent(
        context: Context,
        coroutineDispatcherProvider: CoroutineDispatcherProvider,
        @ProcessLifetime coroutineScope: CoroutineScope,
        newsApiService: NewsApiService
    ): DataComponent = DataComponent.factory().create(
        context = context,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        coroutineScope = coroutineScope,
        newsApiService = newsApiService,
        databaseName = null
    )

    @Provides
    @Reusable
    fun provideStoryRepository(dataComponent: DataComponent): StoryRepository {
        return dataComponent.storyRepository
    }

    @Provides
    @Reusable
    fun provideBookmarkRepository(dataComponent: DataComponent): BookmarkRepository {
        return dataComponent.bookmarkRepository
    }

    @Provides
    @Reusable
    fun headlineStoryStore(dataComponent: DataComponent): HeadlineStoryStore {
        return dataComponent.headlineStoryStore
    }

    @Provides
    @Reusable
    fun personalizedStoryStore(dataComponent: DataComponent): PersonalizedStoryStore {
        return dataComponent.personalizedStoryStore
    }
}
