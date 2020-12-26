package io.github.reactivecircus.streamlined.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.DefaultAnimationConfigs
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import kotlinx.coroutines.Dispatchers
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
abstract class AppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @Reusable
    abstract fun animationConfigs(impl: DefaultAnimationConfigs): AnimationConfigs

    companion object {

        @Provides
        @Reusable
        fun coroutineDispatcherProvider(): CoroutineDispatcherProvider {
            return CoroutineDispatcherProvider(
                io = Dispatchers.IO,
                computation = Dispatchers.Default,
                ui = Dispatchers.Main.immediate
            )
        }

        @Provides
        @Reusable
        fun imageLoader(context: Context): ImageLoader {
            return ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
    }
}
