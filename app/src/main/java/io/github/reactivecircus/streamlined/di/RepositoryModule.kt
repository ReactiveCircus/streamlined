package io.github.reactivecircus.streamlined.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.BuildConfig
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import io.github.reactivecircus.streamlined.ui.di.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
object RepositoryModule {

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
        databaseName = BuildConfig.DATABASE_NAME
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
}
