package io.github.reactivecircus.streamlined.data

import com.dropbox.android.external.store4.Store
import io.github.reactivecircus.streamlined.domain.model.Story

typealias HeadlineStoryStore = Store<Unit, List<Story>>
typealias PersonalizedStoryStore = Store<String, List<Story>>
