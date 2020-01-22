package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.work.scheduler.BackgroundWorkScheduler

/**
 * No-op implementation of [BackgroundWorkScheduler].
 */
class NoOpBackgroundWorkScheduler : BackgroundWorkScheduler {

    override fun scheduleHourlyStorySync() = Unit
}
