package io.github.reactivecircus.streamlined.testing.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService

@Module
internal object TestRepositoryModule {

    @Provides
    @Reusable
    fun provideStoryRepository(
        context: Context,
        newsApiService: NewsApiService
    ): StoryRepository {
        return DataComponent.factory()
            .create(
                context = context,
                newsApiService = newsApiService
            )
            .storyRepository
    }

    @Provides
    @Reusable
    fun provideBookmarkRepository(
        context: Context,
        newsApiService: NewsApiService
    ): BookmarkRepository {
        return DataComponent.factory()
            .create(
                context = context,
                newsApiService = newsApiService
            )
            .bookmarkRepository
    }
}
