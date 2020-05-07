package io.github.reactivecircus.streamlined.testing.di

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import coil.DefaultRequestOptions
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.GetRequest
import coil.request.LoadRequest
import coil.request.RequestDisposable
import coil.request.RequestResult
import coil.request.SuccessResult
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
        fun imageLoader(): ImageLoader {
            return object : ImageLoader {

                private val drawable = ColorDrawable(Color.TRANSPARENT)

                private val disposable = object : RequestDisposable {
                    override val isDisposed = true

                    @OptIn(ExperimentalCoilApi::class)
                    override suspend fun await() = Unit

                    override fun dispose() = Unit
                }

                override val defaults = DefaultRequestOptions()

                override fun clearMemory() = Unit

                override suspend fun execute(request: GetRequest): RequestResult {
                    return SuccessResult(drawable, DataSource.MEMORY_CACHE)
                }

                override fun execute(request: LoadRequest): RequestDisposable {
                    request.target?.onStart(drawable)
                    request.target?.onSuccess(drawable)
                    return disposable
                }

                override fun invalidate(key: String) = Unit

                override fun shutdown() = Unit
            }
        }
    }
}
