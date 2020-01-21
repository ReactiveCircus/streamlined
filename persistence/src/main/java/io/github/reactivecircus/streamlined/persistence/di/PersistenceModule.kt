package io.github.reactivecircus.streamlined.persistence.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.persistence.StoryDao
import io.github.reactivecircus.streamlined.persistence.StoryDaoImpl
import io.github.reactivecircus.streamlined.persistence.StoryEntityQueries
import io.github.reactivecircus.streamlined.persistence.StreamlinedDatabase

@Module(includes = [PersistenceModule.Providers::class])
internal abstract class PersistenceModule {

    @Binds
    abstract fun storyDao(impl: StoryDaoImpl): StoryDao

    @Module
    internal object Providers {

        @Provides
        fun database(context: Context): StreamlinedDatabase {
            return StreamlinedDatabase(AndroidSqliteDriver(StreamlinedDatabase.Schema, context))
        }

        @Provides
        fun storyQueries(database: StreamlinedDatabase): StoryEntityQueries {
            return database.storyEntityQueries
        }
    }
}
