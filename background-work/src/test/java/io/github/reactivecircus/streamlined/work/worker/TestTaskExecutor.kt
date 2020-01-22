package io.github.reactivecircus.streamlined.work.worker

import androidx.work.impl.utils.SerialExecutor
import androidx.work.impl.utils.taskexecutor.TaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TestTaskExecutor : TaskExecutor {

    private val serialExecutor = SerialExecutor(Executors.newSingleThreadExecutor())

    override fun postToMainThread(runnable: Runnable) {
        serialExecutor.execute(runnable)
    }

    override fun getMainThreadExecutor(): Executor {
        return serialExecutor
    }

    override fun executeOnBackgroundThread(runnable: Runnable) {
        serialExecutor.execute(runnable)
    }

    override fun getBackgroundExecutor(): SerialExecutor {
        return serialExecutor
    }
}
