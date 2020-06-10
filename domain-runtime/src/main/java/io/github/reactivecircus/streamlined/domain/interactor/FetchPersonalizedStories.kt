package io.github.reactivecircus.streamlined.domain.interactor

import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import javax.inject.Inject

class FetchPersonalizedStories @Inject constructor(
    private val storyRepository: StoryRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<FetchPersonalizedStories.Params, List<Story>>() {

    override val dispatcher: CoroutineDispatcher = dispatcherProvider.io

    override suspend fun doWork(params: Params): List<Story> {
        return storyRepository.fetchPersonalizedStories(params.query)
    }

    // TODO use custom query / filter type
    class Params(internal val query: String) : InteractorParams
}
