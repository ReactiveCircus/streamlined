package io.github.reactivecircus.streamlined.domain.interactor

import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import javax.inject.Inject

class SyncStories @Inject constructor(
    private val storyRepository: StoryRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<EmptyParams, Unit>() {

    override val dispatcher: CoroutineDispatcher = dispatcherProvider.io

    override suspend fun doWork(params: EmptyParams) {
        // TODO sync stories (headlines and personalized stories) based on applied filters
    }
}
