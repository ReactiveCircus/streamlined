package io.github.reactivecircus.streamlined.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import coil.ImageLoader
import coil.ImageLoaderBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.StreamlinedNavigatorProvider
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.DefaultAnimationConfigs
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import io.github.reactivecircus.streamlined.ui.di.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
abstract class AppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @Reusable
    abstract fun navigatorProvider(impl: StreamlinedNavigatorProvider): NavigatorProvider

    @Binds
    @Reusable
    abstract fun animationConfigs(impl: DefaultAnimationConfigs): AnimationConfigs

    companion object {

        @Provides
        @Reusable
        fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
            return CoroutineDispatcherProvider(
                io = Dispatchers.IO,
                computation = Dispatchers.Default,
                ui = Dispatchers.Main.immediate
            )
        }

        @Provides
        @Reusable
        @ProcessLifetime
        fun provideAppCoroutineScope(): CoroutineScope {
            return ProcessLifecycleOwner.get().lifecycle.coroutineScope
        }

        @Provides
        @Reusable
        fun provideImageLoader(context: Context): ImageLoader {
            return ImageLoaderBuilder(context).build()
        }
    }
}
