package io.github.reactivecircus.streamlined.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.StreamlinedNavigator
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.DefaultAnimationConfigs
import kotlinx.coroutines.Dispatchers
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module(includes = [AppModule.Providers::class])
abstract class AppModule {

    @Binds
    abstract fun navigator(impl: StreamlinedNavigator): Navigator

    @Binds
    abstract fun animationConfigs(impl: DefaultAnimationConfigs): AnimationConfigs

    @Module
    object Providers {

        @Provides
        fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
            return CoroutineDispatcherProvider(
                io = Dispatchers.IO,
                computation = Dispatchers.Default,
                ui = Dispatchers.Main.immediate
            )
        }
    }
}
