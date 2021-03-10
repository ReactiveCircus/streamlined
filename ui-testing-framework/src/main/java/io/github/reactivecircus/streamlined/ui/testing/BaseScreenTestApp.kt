package io.github.reactivecircus.streamlined.ui.testing

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.Coil.setImageLoader
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
import timber.log.Timber

abstract class BaseScreenTestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Timber
        Timber.plant(TestDebugTree())

        // set default image loader
        setImageLoader(FakeImageLoader)
    }
}

private val FakeImageLoader = object : ImageLoader {

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
