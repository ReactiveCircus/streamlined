package io.github.reactivecircus.streamlined.di

import android.content.Context
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.streamlined.BuildConfig
import io.github.reactivecircus.streamlined.persistence.DatabaseConfigs
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.DefaultAnimationConfigs
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Reusable
    abstract fun animationConfigs(impl: DefaultAnimationConfigs): AnimationConfigs

    companion object {

        @Provides
        @Singleton
        fun coroutineDispatcherProvider(): CoroutineDispatcherProvider {
            return CoroutineDispatcherProvider(
                io = Dispatchers.IO,
                computation = Dispatchers.Default,
                ui = Dispatchers.Main.immediate
            )
        }

        @Provides
        @Singleton
        fun imageLoader(@ApplicationContext context: Context): ImageLoader {
            return ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }

        @Provides
        @Reusable
        fun databaseConfigs(
            coroutineDispatcherProvider: CoroutineDispatcherProvider
        ): DatabaseConfigs {
            return DatabaseConfigs(
                databaseName = BuildConfig.DATABASE_NAME,
                coroutineContext = coroutineDispatcherProvider.io,
            )
        }
    }
}
