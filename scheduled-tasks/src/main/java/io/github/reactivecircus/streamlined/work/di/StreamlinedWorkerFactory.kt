package io.github.reactivecircus.streamlined.work.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class StreamlinedWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val entry = workerFactories.entries.find {
            Class.forName(workerClassName).isAssignableFrom(it.key)
        }

        val factoryProvider = requireNotNull(entry?.value) {
            "Unknown worker class name: $workerClassName"
        }

        return factoryProvider.get().create(appContext, workerParameters)
    }
}
