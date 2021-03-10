@file:Suppress("DEPRECATION")

package io.github.reactivecircus.streamlined.ui.testing.di

import android.content.Context
import android.os.AsyncTask
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.streamlined.persistence.DatabaseConfigs
import io.github.reactivecircus.streamlined.ui.testing.TestAnimationConfigs
import io.github.reactivecircus.streamlined.ui.common.configs.AnimationConfigs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TestAppModule {

    @Binds
    @Reusable
    abstract fun animationConfigs(impl: TestAnimationConfigs): AnimationConfigs

    companion object {

        @Suppress("DEPRECATION")
        @Provides
        @Reusable
        fun coroutineDispatcherProvider(): CoroutineDispatcherProvider {
            // TODO use proper io dispatcher https://github.com/Kotlin/kotlinx.coroutines/issues/242
            return CoroutineDispatcherProvider(
                io = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher(),
                computation = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher(),
                ui = Dispatchers.Main.immediate
            )
        }

        @Provides
        @Reusable
        fun imageLoader(@ApplicationContext context: Context): ImageLoader {
            return ImageLoader.Builder(context).build()
        }

        @Provides
        @Reusable
        fun databaseConfigs(
            coroutineDispatcherProvider: CoroutineDispatcherProvider
        ): DatabaseConfigs {
            return DatabaseConfigs(
                databaseName = null,
                coroutineContext = coroutineDispatcherProvider.io,
            )
        }
    }
}
