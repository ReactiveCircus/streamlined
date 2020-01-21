package io.github.reactivecircus.streamlined.data.di

import android.content.Context
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.data.mapper.toEntity
import io.github.reactivecircus.streamlined.data.repository.StoryRepositoryImpl
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.persistence.di.PersistenceComponent
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Module(includes = [DataModule.Providers::class])
internal abstract class DataModule {

    @Binds
    abstract fun storyRepository(impl: StoryRepositoryImpl): StoryRepository

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
        ): Store<Unit, List<StoryEntity>> {
            return StoreBuilder.fromNonFlow<Unit, List<StoryEntity>>(
                fetcher = {
                    newsApiService.headlines().map { it.toEntity() }
                }
            ).persister(
                reader = {
                    storyDao.allStories()
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
