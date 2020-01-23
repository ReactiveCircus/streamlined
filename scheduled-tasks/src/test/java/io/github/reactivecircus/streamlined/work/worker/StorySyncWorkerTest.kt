package io.github.reactivecircus.streamlined.work.worker

import androidx.work.ListenableWorker
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.streamlined.domain.interactor.SyncStories
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams
import java.io.IOException

class StorySyncWorkerTest {

    private val syncStories = mockk<SyncStories>()

    private val worker = StorySyncWorker(
        appContext = mockk(),
        params = mockk {
            every { taskExecutor } returns TestTaskExecutor()
        },
        syncStories = syncStories
    )

    @Test
    fun `should Result#Success when StorySyncWorker runs successfully`() {
        coEvery { syncStories.execute(any()) } returns Unit

        val result = worker.startWork().get()

        coVerify(exactly = 1) {
            syncStories.execute(EmptyParams)
        }

        assertThat(result).isEqualTo(ListenableWorker.Result.success())
    }

    @Test
    fun `should Result#Failure when StorySyncWorker fails`() {
        coEvery { syncStories.execute(any()) } throws IOException()

        val result = worker.startWork().get()

        coVerify(exactly = 1) {
            syncStories.execute(EmptyParams)
        }

        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
    }
}
