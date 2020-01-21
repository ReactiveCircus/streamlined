package io.github.reactivecircus.streamlined.data.di

import android.content.Context
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.data.mapper.toEntity
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.data.repository.BookmarkRepositoryImpl
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.persistence.di.PersistenceComponent
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

@Module(includes = [DataModule.Providers::class])
internal abstract class DataModule {

    @Binds
    abstract fun bookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Module
    internal object Providers {

        @Provides
        fun storyDao(context: Context): StoryDao {
            return PersistenceComponent.factory()
                .create(context)
                .storyDao
        }

        @Provides
        @FlowPreview
        @ExperimentalCoroutinesApi
        fun storiesStore(
            newsApiService: NewsApiService,
            storyDao: StoryDao
        ): Store<Unit, List<Story>> {
            return StoreBuilder.fromNonFlow<Unit, List<StoryEntity>>(
                fetcher = {
                    newsApiService.headlines().map { it.toEntity() }
                }
            ).persister(
                reader = {
                    storyDao.allStories().map { stories ->
                        stories.map { it.toModel() }
                    }
                },
                writer = { _, stories ->
                    storyDao.updateStories(stories)
                },
                delete = {
                    storyDao.deleteAll()
                }
            ).build()
        }
    }
}
