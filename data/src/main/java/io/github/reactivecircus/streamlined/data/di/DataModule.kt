package io.github.reactivecircus.streamlined.data.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.store.ext.RefreshPolicy
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.data.TimeBasedRefreshPolicy
import io.github.reactivecircus.streamlined.data.mapper.toEntity
import io.github.reactivecircus.streamlined.data.mapper.toModel
import io.github.reactivecircus.streamlined.data.repository.BookmarkRepositoryImpl
import io.github.reactivecircus.streamlined.data.repository.StoryRepositoryImpl
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    @FlowPreview
    @ExperimentalCoroutinesApi
    abstract fun storyRepository(impl: StoryRepositoryImpl): StoryRepository

    @Binds
    @Singleton
    abstract fun bookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    internal companion object {

        @Provides
        @Singleton
        @InternalApi
        fun longLifeCoroutineScope(
            coroutineDispatcherProvider: CoroutineDispatcherProvider
        ): CoroutineScope {
            return CoroutineScope(SupervisorJob() + coroutineDispatcherProvider.io)
        }

        @Provides
        @Singleton
        fun refreshPolicy(): RefreshPolicy {
            return TimeBasedRefreshPolicy()
        }

        @Provides
        @Singleton
        @FlowPreview
        @ExperimentalCoroutinesApi
        fun headlineStoryStore(
            newsApiService: NewsApiService,
            storyDao: StoryDao,
            @InternalApi longLifetimeCoroutineScope: CoroutineScope
        ): HeadlineStoryStore {
            return StoreBuilder.from<Unit, List<StoryEntity>, List<Story>>(
                fetcher = Fetcher.of {
                    // TODO source country (hardcode ISO 3166-1 country code) from user preference
                    val country = "au"
                    newsApiService.headlines(country).stories.map { it.toEntity(isHeadline = true) }
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = {
                        storyDao.headlineStories().map { stories ->
                            stories.map { it.toModel() }.ifEmpty { null }
                        }
                    },
                    writer = { _, stories ->
                        storyDao.updateStories(forHeadlines = true, stories = stories)
                    },
                    deleteAll = storyDao::deleteHeadlineStories
                )
            )
                .scope(longLifetimeCoroutineScope)
                .build()
        }

        @Provides
        @Singleton
        @FlowPreview
        @ExperimentalCoroutinesApi
        fun personalizedStoryStore(
            newsApiService: NewsApiService,
            storyDao: StoryDao,
            @InternalApi longLifetimeCoroutineScope: CoroutineScope,
        ): PersonalizedStoryStore {
            return StoreBuilder.from<String, List<StoryEntity>, List<Story>>(
                fetcher = Fetcher.of { query ->
                    // TODO use custom query type instead of string
                    newsApiService.everything(query).stories.map { it.toEntity(isHeadline = false) }
                },
                sourceOfTruth = SourceOfTruth.of(
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
                    deleteAll = storyDao::deleteNonHeadlineStories
                )
            )
                .scope(longLifetimeCoroutineScope)
                .build()
        }
    }
}
