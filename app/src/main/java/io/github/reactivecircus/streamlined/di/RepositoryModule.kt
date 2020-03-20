package io.github.reactivecircus.streamlined.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.store.ext.RefreshCriteria
import io.github.reactivecircus.streamlined.BuildConfig
import io.github.reactivecircus.streamlined.data.TimeBasedRefreshCriteria
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import kotlinx.coroutines.GlobalScope
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module
object RepositoryModule {

    @Provides
    @Reusable
    @InternalApi
    fun provideRefreshCriteria(): RefreshCriteria {
        return TimeBasedRefreshCriteria()
    }

    @Provides
    @Reusable
    fun provideDataComponent(
        context: Context,
        coroutineDispatcherProvider: CoroutineDispatcherProvider,
        @InternalApi refreshCriteria: RefreshCriteria,
        newsApiService: NewsApiService
    ): DataComponent = DataComponent.factory().create(
        context = context,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        processLifetimeCoroutineScope = GlobalScope,
        refreshCriteria = refreshCriteria,
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
