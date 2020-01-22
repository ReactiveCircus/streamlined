package io.github.reactivecircus.streamlined.domain.interactor

import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import javax.inject.Inject

class GetStoryById @Inject constructor(
    private val storyRepository: StoryRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<GetStoryById.Params, Story>() {

    override val dispatcher: CoroutineDispatcher = dispatcherProvider.io

    override suspend fun doWork(params: Params): Story {
        return storyRepository.getStoryById(params.id)
            ?: throw NoSuchElementException("Could not found story for id ${params.id}")
    }

    class Params(internal val id: Long) : InteractorParams
}
