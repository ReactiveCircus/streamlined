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
            repeatInterval = 60, repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 30, flexTimeIntervalUnit = TimeUnit.MINUTES
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
}
