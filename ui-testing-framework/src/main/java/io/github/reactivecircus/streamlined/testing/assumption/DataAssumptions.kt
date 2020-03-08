package io.github.reactivecircus.streamlined.testing.assumption

import androidx.test.core.app.ApplicationProvider
import com.dropbox.android.external.store4.ExperimentalStoreApi
import com.dropbox.android.external.store4.fresh
import io.github.reactivecircus.streamlined.data.HeadlineStoryStore
import io.github.reactivecircus.streamlined.data.PersonalizedStoryStore
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject

@OptIn(ExperimentalStoreApi::class)
class DataAssumptions @Inject constructor(
    private val headlineStoryStore: HeadlineStoryStore,
    private val personalizedStoryStore: PersonalizedStoryStore,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {
    internal fun assumeNoCachedHeadlineStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            headlineStoryStore.clearAll()
        }
    }

    internal fun assumeNoCachedPersonalizedStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            personalizedStoryStore.clearAll()
        }
    }

    internal fun populateHeadlineStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            headlineStoryStore.fresh(Unit)
        }
    }

    internal fun populatePersonalizedStories() = runBlocking {
        withContext(dispatcherProvider.io) {
            personalizedStoryStore.fresh(DUMMY_QUERY)
        }
    }
}

private const val DUMMY_QUERY = "query"

private val dataAssumptions: DataAssumptions = TestingFrameworkComponent
    .getOrCreate(ApplicationProvider.getApplicationContext())
    .dataAssumptions

fun assumeNoCachedHeadlineStories() {
    dataAssumptions.assumeNoCachedHeadlineStories()
}

fun assumeNoCachedPersonalizedStories() {
    dataAssumptions.assumeNoCachedPersonalizedStories()
}

fun populateHeadlineStories() {
    dataAssumptions.populateHeadlineStories()
}

fun populatePersonalizedStories() {
    dataAssumptions.populatePersonalizedStories()
}
