package io.github.reactivecircus.streamlined.domain.interactor

import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.coroutines.FlowInteractor
import javax.inject.Inject

class StreamHeadlineStories @Inject constructor(
    private val storyRepository: StoryRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : FlowInteractor<EmptyParams, StoreResponse<List<Story>>>() {

    override val dispatcher: CoroutineDispatcher = dispatcherProvider.io

    override fun createFlow(params: EmptyParams): Flow<StoreResponse<List<Story>>> {
        return storyRepository.streamHeadlineStories()
    }
}
