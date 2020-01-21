package io.github.reactivecircus.streamlined.di

import android.app.Application
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
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
}
