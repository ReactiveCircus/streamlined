package io.github.reactivecircus.streamlined.persistence.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.streamlined.persistence.DatabaseConfigs
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryDaoImpl
import io.github.reactivecircus.streamlined.persistence.StoryEntityQueries
import io.github.reactivecircus.streamlined.persistence.StreamlinedDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PersistenceModule {

    @Binds
    @Reusable
    abstract fun storyDao(impl: StoryDaoImpl): StoryDao

    internal companion object {

        @Provides
        @Singleton
        fun database(
            @ApplicationContext context: Context,
            databaseConfigs: DatabaseConfigs,
        ): StreamlinedDatabase {
            return StreamlinedDatabase(
                AndroidSqliteDriver(
                    StreamlinedDatabase.Schema,
                    context,
                    databaseConfigs.databaseName,
                )
            )
        }

        @Provides
        @Reusable
        fun storyQueries(database: StreamlinedDatabase): StoryEntityQueries {
            return database.storyEntityQueries
        }
    }
}
