package io.github.reactivecircus.streamlined.di

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.StreamlinedNavigator
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.DefaultAnimationConfigs
import io.github.reactivecircus.streamlined.ui.di.DaggerFragmentFactory
import kotlinx.coroutines.Dispatchers
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module(includes = [AppModule.Providers::class])
abstract class AppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DaggerFragmentFactory): FragmentFactory

    @Binds
    @Reusable
    abstract fun navigator(impl: StreamlinedNavigator): Navigator

    @Binds
    @Reusable
    abstract fun animationConfigs(impl: DefaultAnimationConfigs): AnimationConfigs

    @Module
    object Providers {

        @Provides
        @Reusable
        fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
            return CoroutineDispatcherProvider(
                io = Dispatchers.IO,
                computation = Dispatchers.Default,
                ui = Dispatchers.Main.immediate
            )
        }
    }
}
