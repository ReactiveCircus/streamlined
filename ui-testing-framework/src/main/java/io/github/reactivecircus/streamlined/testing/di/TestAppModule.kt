package io.github.reactivecircus.streamlined.testing.di

import android.os.AsyncTask
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.testing.TestAnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Module
internal abstract class TestAppModule {

    @Binds
    @Reusable
    abstract fun animationConfigs(impl: TestAnimationConfigs): AnimationConfigs

    companion object {

        @Provides
        @Reusable
        fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
            // TODO use proper io dispatcher https://github.com/Kotlin/kotlinx.coroutines/issues/242
            return CoroutineDispatcherProvider(
                io = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher(),
                computation = Dispatchers.Default,
                ui = Dispatchers.Main.immediate
            )
        }
    }
}
