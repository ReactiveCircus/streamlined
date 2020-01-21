package io.github.reactivecircus.streamlined.di

import android.app.Application
import com.dropbox.android.external.store4.Store
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.data.di.DataComponent
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideStoryStore(
        application: Application,
        newsApiService: NewsApiService
    ): Store<Unit, List<Story>> {
        return DataComponent.factory()
            .create(
                context = application,
                newsApiService = newsApiService
            )
            .storyStore
    }

    @Provides
    @Singleton
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
