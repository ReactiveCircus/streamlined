package io.github.reactivecircus.streamlined.testing.di

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import coil.DefaultRequestOptions
import coil.ImageLoader
import coil.annotation.ExperimentalCoil
import coil.request.GetRequest
import coil.request.LoadRequest
import coil.request.RequestDisposable
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.reactivecircus.streamlined.testing.TestAnimationConfigs
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.di.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
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

        @Provides
        @Reusable
        @ProcessLifetime
        fun provideAppCoroutineScope(): CoroutineScope {
            return ProcessLifecycleOwner.get().lifecycle.coroutineScope
        }

        @Provides
        @Reusable
        fun provideImageLoader(): ImageLoader {
            return object : ImageLoader {

                private val drawable = ColorDrawable(Color.TRANSPARENT)

                private val disposable = object : RequestDisposable {
                    override val isDisposed = true

                    @OptIn(ExperimentalCoil::class)
                    override suspend fun await() = Unit

                    override fun dispose() = Unit
                }

                override val defaults = DefaultRequestOptions()

                override suspend fun get(request: GetRequest) = drawable

                override fun load(request: LoadRequest): RequestDisposable {
                    request.target?.onStart(drawable)
                    request.target?.onSuccess(drawable)
                    return disposable
                }

                override fun clearMemory() = Unit

                override fun shutdown() = Unit
            }
        }
    }
}
