package io.github.reactivecircus.streamlined.testing.di

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
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
        processLifetimeCoroutineScope = ProcessLifecycleOwner.get().lifecycle.coroutineScope,
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
