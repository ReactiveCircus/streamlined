package io.github.reactivecircus.streamlined.work.scheduler

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.work.worker.StorySyncWorker
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

class DefaultBackgroundWorkSchedulerTest {

    private val workManager = mockk<WorkManager>(relaxed = true)

    private val backgroundWorkScheduler = DefaultBackgroundWorkScheduler(workManager)

    @Test
    fun `schedule hourly story sync`() {
        backgroundWorkScheduler.scheduleHourlyStorySync()

        val slot = slot<PeriodicWorkRequest>()

        verify(exactly = 1) {
            workManager.enqueueUniquePeriodicWork(
                StorySyncWorker.TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                capture(slot)
            )
        }

        val workSpec = slot.captured.workSpec

        assertThat(workSpec.intervalDuration)
            .isEqualTo(60 * 60 * 1000)

        assertThat(workSpec.flexDuration)
            .isEqualTo(30 * 60 * 1000)

        assertThat(workSpec.constraints.requiredNetworkType)
            .isEqualTo(NetworkType.UNMETERED)

        assertThat(workSpec.constraints.requiresBatteryNotLow())
            .isEqualTo(true)
    }
}
