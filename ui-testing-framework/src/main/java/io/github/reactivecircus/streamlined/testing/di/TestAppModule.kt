@file:Suppress("DEPRECATION")

package io.github.reactivecircus.streamlined.testing.di

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
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
        fun imageLoader(): ImageLoader {
            return object : ImageLoader {

                private val drawable = ColorDrawable(Color.TRANSPARENT)

                private val disposable = object : Disposable {
                    override val isDisposed = true

                    @OptIn(ExperimentalCoilApi::class)
                    override suspend fun await() = Unit

                    override fun dispose() = Unit
                }

                override val defaults = DefaultRequestOptions()

                override val memoryCache get() = throw UnsupportedOperationException()

                override val bitmapPool = BitmapPool(0)

                override fun enqueue(request: ImageRequest): Disposable {
                    request.target?.onStart(drawable)
                    request.target?.onSuccess(drawable)
                    return disposable
                }

                override suspend fun execute(request: ImageRequest): ImageResult {
                    return SuccessResult(
                        drawable = drawable,
                        request = request,
                        metadata = ImageResult.Metadata(
                            memoryCacheKey = MemoryCache.Key(""),
                            isSampled = false,
                            dataSource = DataSource.MEMORY_CACHE,
                            isPlaceholderMemoryCacheKeyPresent = false
                        )
                    )
                }

                override fun shutdown() = Unit
            }
        }
    }
}
