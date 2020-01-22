package io.github.reactivecircus.streamlined.storydetails

import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.github.reactivecircus.streamlined.domain.interactor.GetStoryById

class StoryDetailsViewModel @AssistedInject constructor(
    @Assisted private val id: Long,
    getStoryById: GetStoryById
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(id: Long): StoryDetailsViewModel
    }
}
