package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.ViewModel
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories
) : ViewModel()
