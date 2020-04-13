package io.github.reactivecircus.streamlined.testing.di

import android.content.Context
import android.os.AsyncTask
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import io.github.reactivecircus.streamlined.testing.TestRefreshPolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
internal object TestRepositoryModule {

    @Provides
    @Reusable
    fun dataComponent(
        context: Context,
        coroutineDispatcherProvider: CoroutineDispatcherProvider,
        newsApiService: NewsApiService
    ): DataComponent = DataComponent.factory().create(
        context = context,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        longLifetimeCoroutineScope = CoroutineScope(
            SupervisorJob() + AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
        ),
        refreshPolicy = TestRefreshPolicy,
        newsApiService = newsApiService,
        databaseName = null
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
