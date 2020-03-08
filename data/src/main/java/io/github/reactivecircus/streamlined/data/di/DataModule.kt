package io.github.reactivecircus.streamlined.data.di

import android.content.Context
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.mapper.toEntity
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.data.repository.BookmarkRepositoryImpl
import io.github.reactivecircus.streamlined.data.repository.StoryRepositoryImpl
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.persistence.di.PersistenceComponent
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module
internal abstract class DataModule {

    @Binds
    @Reusable
    abstract fun storyRepository(impl: StoryRepositoryImpl): StoryRepository

    @Binds
    @Reusable
    abstract fun bookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    internal companion object {

        @Provides
        @InternalApi
        fun providePersistenceComponent(
            context: Context,
            coroutineDispatcherProvider: CoroutineDispatcherProvider,
            databaseName: String?
        ): PersistenceComponent = PersistenceComponent.factory()
            .create(
                context = context,
                coroutineContext = coroutineDispatcherProvider.io,
                databaseName = databaseName
            )

        @Provides
        @Reusable
        fun storyDao(@InternalApi persistenceComponent: PersistenceComponent): StoryDao {
            return persistenceComponent.storyDao
        }

        @Provides
        @Reusable
        @FlowPreview
        @ExperimentalCoroutinesApi
        fun headlineStoryStore(
            newsApiService: NewsApiService,
            storyDao: StoryDao
        ): HeadlineStoryStore {
            return StoreBuilder.fromNonFlow<Unit, List<StoryEntity>>(
                fetcher = {
                    // TODO source country (hardcode ISO 3166-1 country code) from user preference
                    val country = "au"
                    newsApiService.headlines(country).stories.map { it.toEntity(isHeadline = true) }
                }
            ).persister(
                reader = {
                    storyDao.headlineStories().map { stories ->
                        if (stories.isNotEmpty()) {
                            stories.map { it.toModel() }
                        } else {
                            null
                        }
                    }
                },
                writer = { _, stories ->
                    storyDao.updateStories(forHeadlines = true, stories = stories)
                },
                deleteAll = {
                    storyDao.deleteHeadlineStories()
                }
            ).build()
        }

        @Provides
        @Reusable
        @FlowPreview
        @ExperimentalCoroutinesApi
        fun personalizedStoryStore(
            newsApiService: NewsApiService,
            storyDao: StoryDao
        ): PersonalizedStoryStore {
            return StoreBuilder.fromNonFlow<String, List<StoryEntity>>(
                fetcher = { query ->
                    // TODO use custom query type instead of string
                    newsApiService.everything(query).stories.map { it.toEntity(isHeadline = false) }
                }
            ).persister(
                reader = {
                    storyDao.nonHeadlineStories().map { stories ->
                        if (stories.isNotEmpty()) {
                            stories.map { it.toModel() }
                        } else {
                            null
                        }
                    }
                },
                writer = { _, stories ->
                    storyDao.updateStories(forHeadlines = false, stories = stories)
                },
                deleteAll = {
                    storyDao.deleteNonHeadlineStories()
                }
            ).build()
        }
    }
}
