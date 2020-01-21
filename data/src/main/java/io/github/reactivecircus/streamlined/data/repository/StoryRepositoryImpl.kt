package io.github.reactivecircus.streamlined.data.repository

import com.dropbox.android.external.store4.Store
import io.github.reactivecircus.streamlined.domain.repository.StoryRepository
import io.github.reactivecircus.streamlined.persistence.StoryEntity
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val storyStore: Store<Unit, List<StoryEntity>>
) : StoryRepository
