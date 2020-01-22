package io.github.reactivecircus.streamlined.work.scheduler

interface BackgroundWorkScheduler {

    /**
     * Schedules hourly stories syncing in the background.
     */
    fun scheduleHourlyStorySync()
}
