package io.github.reactivecircus.streamlined.domain.interactor

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.model.Story
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.coroutines.FlowInteractor
import javax.inject.Inject

class StreamHeadlineStories @Inject constructor(
    private val storyStore: Store<Unit, List<Story>>,
    dispatcherProvider: CoroutineDispatcherProvider
) : FlowInteractor<EmptyParams, StoreResponse<List<Story>>>() {

    override val dispatcher: CoroutineDispatcher = dispatcherProvider.io

    override fun createFlow(params: EmptyParams): Flow<StoreResponse<List<Story>>> {
        return storyStore.stream(StoreRequest.cached(Unit, refresh = true))
    }
}
