package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.model.Story
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import reactivecircus.blueprint.interactor.EmptyParams
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories
) : ViewModel() {

    val state: LiveData<StoreResponse<List<Story>>> = streamHeadlineStories.buildFlow(EmptyParams)
        .catch { Timber.e(it, "flow cancelled") }
        .asLiveData()
}
