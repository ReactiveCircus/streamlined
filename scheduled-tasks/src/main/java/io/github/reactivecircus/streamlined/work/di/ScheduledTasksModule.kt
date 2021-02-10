package io.github.reactivecircus.streamlined.work.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.streamlined.work.scheduler.DefaultTaskScheduler
import io.github.reactivecircus.streamlined.work.scheduler.TaskScheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduledTasksModule {

    @Binds
    @Singleton
    abstract fun taskScheduler(impl: DefaultTaskScheduler): TaskScheduler

    companion object {

        @Provides
        @Singleton
        fun workManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}
