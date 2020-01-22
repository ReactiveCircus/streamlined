package io.github.reactivecircus.streamlined.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService

@Module
object RepositoryModule {

    @Provides
    @Reusable
    fun provideStoryRepository(
        application: Application,
        newsApiService: NewsApiService
    ): StoryRepository {
        return DataComponent.factory()
            .create(
                context = application,
                newsApiService = newsApiService
            )
            .storyRepository
    }

    @Provides
    @Reusable
    fun provideBookmarkRepository(
        application: Application,
        newsApiService: NewsApiService
    ): BookmarkRepository {
        return DataComponent.factory()
            .create(
                context = application,
                newsApiService = newsApiService
            )
            .bookmarkRepository
    }
}
