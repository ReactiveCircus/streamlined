package io.github.reactivecircus.streamlined.work.scheduler

interface TaskScheduler {

    /**
     * Schedules hourly stories syncing in the background.
     */
    fun scheduleHourlyStorySync()
}
