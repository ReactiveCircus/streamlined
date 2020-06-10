package io.github.reactivecircus.streamlined.domain.interactor

import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import javax.inject.Inject

class FetchHeadlineStories @Inject constructor(
    private val storyRepository: StoryRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<EmptyParams, List<Story>>() {

    override val dispatcher: CoroutineDispatcher = dispatcherProvider.io

    override suspend fun doWork(params: EmptyParams): List<Story> {
        return storyRepository.fetchHeadlineStories()
    }
}
