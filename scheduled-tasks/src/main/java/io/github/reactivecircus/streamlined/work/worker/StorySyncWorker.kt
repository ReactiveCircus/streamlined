package io.github.reactivecircus.streamlined.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.reactivecircus.streamlined.domain.interactor.SyncStories
import reactivecircus.blueprint.interactor.EmptyParams

@HiltWorker
class StorySyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val syncStories: SyncStories
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        syncStories.execute(EmptyParams)
        return Result.success()
    }

    companion object {
        const val TAG = "story-sync"
    }
}
