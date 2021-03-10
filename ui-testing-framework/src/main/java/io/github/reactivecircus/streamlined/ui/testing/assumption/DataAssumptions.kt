package io.github.reactivecircus.streamlined.ui.testing.assumption

import com.dropbox.android.external.store4.ExperimentalStoreApi
import com.dropbox.android.external.store4.fresh
import dagger.Reusable
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@Reusable
@OptIn(ExperimentalStoreApi::class)
class DataAssumptions @Inject constructor(
    private val headlineStoryStore: HeadlineStoryStore,
    private val personalizedStoryStore: PersonalizedStoryStore,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {
    fun assumeNoCachedHeadlineStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            headlineStoryStore.clearAll()
        }
    }

    fun assumeNoCachedPersonalizedStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            personalizedStoryStore.clearAll()
        }
    }

    fun populateHeadlineStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            headlineStoryStore.fresh(Unit)
        }
    }

    fun populatePersonalizedStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            personalizedStoryStore.fresh(DUMMY_QUERY)
        }
    }
}

private const val DUMMY_QUERY = "query"
