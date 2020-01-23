package io.github.reactivecircus.streamlined.work.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import io.github.reactivecircus.streamlined.domain.interactor.SyncStories
import io.github.reactivecircus.streamlined.work.di.ChildWorkerFactory
import reactivecircus.blueprint.interactor.EmptyParams
import javax.inject.Inject
import javax.inject.Provider

class StorySyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val syncStories: SyncStories
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return runCatching {
            syncStories.execute(EmptyParams)
            Result.success()
        }.getOrElse {
            Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val syncStories: Provider<SyncStories>
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return StorySyncWorker(
                appContext,
                params,
                syncStories.get()
            )
        }
    }

    companion object {
        const val TAG = "story-sync"
    }
}
