package io.github.reactivecircus.streamlined.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.store.ext.RefreshPolicy
import io.github.reactivecircus.streamlined.BuildConfig
import io.github.reactivecircus.streamlined.data.TimeBasedRefreshPolicy
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
    fun refreshPolicy(): RefreshPolicy {
        return TimeBasedRefreshPolicy()
    }

    @Provides
    @Reusable
    fun dataComponent(
        context: Context,
        coroutineDispatcherProvider: CoroutineDispatcherProvider,
        @InternalApi refreshPolicy: RefreshPolicy,
        newsApiService: NewsApiService
    ): DataComponent = DataComponent.factory().create(
        context = context,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        processLifetimeCoroutineScope = GlobalScope,
        refreshPolicy = refreshPolicy,
        newsApiService = newsApiService,
        databaseName = BuildConfig.DATABASE_NAME
    )

    @Provides
    @Reusable
    fun storyRepository(dataComponent: DataComponent): StoryRepository {
        return dataComponent.storyRepository
    }

    @Provides
    @Reusable
    fun bookmarkRepository(dataComponent: DataComponent): BookmarkRepository {
        return dataComponent.bookmarkRepository
    }
}
