package io.github.reactivecircus.streamlined.persistence.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryDaoImpl
import io.github.reactivecircus.streamlined.persistence.StoryEntityQueries
import io.github.reactivecircus.streamlined.persistence.StreamlinedDatabase
import javax.inject.Singleton

@Module
internal abstract class PersistenceModule {

    @Binds
    @Reusable
    abstract fun storyDao(impl: StoryDaoImpl): StoryDao

    internal companion object {

        @Provides
        @Singleton
        fun database(context: Context, databaseName: String?): StreamlinedDatabase {
            return StreamlinedDatabase(
                AndroidSqliteDriver(
                    StreamlinedDatabase.Schema,
                    context,
                    databaseName
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
