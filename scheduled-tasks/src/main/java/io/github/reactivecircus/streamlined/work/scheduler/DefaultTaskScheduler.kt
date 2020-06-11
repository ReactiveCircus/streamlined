package io.github.reactivecircus.streamlined.work.scheduler

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import io.github.reactivecircus.streamlined.work.worker.StorySyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DefaultTaskScheduler @Inject constructor(
    private val workManager: WorkManager
) : TaskScheduler {

    override fun scheduleHourlyStorySync() {
        val request = PeriodicWorkRequestBuilder<StorySyncWorker>(
            repeatInterval = SYNC_REPEAT_INTERVAL_MINUTES,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = SYNC_FLEX_TIME_INTERVAL_MINUTES,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            StorySyncWorker.TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        private const val SYNC_REPEAT_INTERVAL_MINUTES = 60L
        private const val SYNC_FLEX_TIME_INTERVAL_MINUTES = 30L
    }
}
