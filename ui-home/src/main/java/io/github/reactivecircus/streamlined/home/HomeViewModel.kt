package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.ui.util.AdapterItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories
) : ViewModel() {

    val state: LiveData<HomeState> = flow {
        val headlines = listOf(
            Story(
                id = 1,
                source = "DEV Community",
                title = "Testing Kotlin Lambda Invocations without Mocking",
                author = "Yang",
                description = "In this post I want to share a simple technique for verifying the invocation of a Kotlin lambda function without mocking it.",
                url = "https://dev.to/ychescale9/testing-kotlin-lambda-invocations-without-mocking-3n32",
                imageUrl = "https://www.abc.net.au/news/image/11991876-16x9-700x394.jpg",
                publishedTime = 1_581_193_802_000L
            ),
            Story(
                id = 2,
                source = "DEV Community",
                title = "Exploring Cirrus CI for Android",
                author = "Yang",
                description = "It's almost 2020 and it remains a challenge to run Android Instrumented tests on CI, especially for opensource projects and small teams.",
                url = "https://dev.to/ychescale9/exploring-cirrus-ci-for-android-434",
                imageUrl = "https://thepracticaldev.s3.amazonaws.com/i/xdjuoqui009himpyrzmm.png",
                publishedTime = 1_581_193_702_000L
            ),
            Story(
                id = 3,
                source = "DEV Community",
                title = "Running Android Instrumented Tests on CI - from Bitrise.io to GitHub Actions",
                author = "Yang",
                description = "I recently wrote about my experience using various cloud-based CI services for running Android instrumented tests on CI for opensource projects, and how I landed on a solution using a custom GitHub Action.",
                url = "https://dev.to/ychescale9/running-android-emulators-on-ci-from-bitrise-io-to-github-actions-3j76",
                imageUrl = "https://thepracticaldev.s3.amazonaws.com/i/vcvesdpw4bmtaoi5an7a.png",
                publishedTime = 1_581_193_662_000L
            )
        )

        val forYou = listOf(
            Story(
                id = 4,
                source = "DEV Community",
                title = "Testing Kotlin Lambda Invocations without Mocking",
                author = "Yang",
                description = "In this post I want to share a simple technique for verifying the invocation of a Kotlin lambda function without mocking it.",
                url = "https://dev.to/ychescale9/testing-kotlin-lambda-invocations-without-mocking-3n32",
                imageUrl = "https://images.idgesg.net/images/article/2020/02/android-11-100832680-large.jpg",
                publishedTime = 1_581_193_812_000L
            ),
            Story(
                id = 5,
                source = "DEV Community",
                title = "Exploring Cirrus CI for Android",
                author = "Yang",
                description = "It's almost 2020 and it remains a challenge to run Android Instrumented tests on CI, especially for opensource projects and small teams.",
                url = "https://dev.to/ychescale9/exploring-cirrus-ci-for-android-434",
                imageUrl = null,
                publishedTime = 1_581_193_752_000L
            ),
            Story(
                id = 6,
                source = "DEV Community",
                title = "Running Android Instrumented Tests on CI - from Bitrise.io to GitHub Actions",
                author = "Yang",
                description = "I recently wrote about my experience using various cloud-based CI services for running Android instrumented tests on CI for opensource projects, and how I landed on a solution using a custom GitHub Action.",
                url = "https://dev.to/ychescale9/running-android-emulators-on-ci-from-bitrise-io-to-github-actions-3j76",
                imageUrl = "https://i0.wp.com/9to5google.com/wp-content/uploads/sites/4/2020/02/Android-11-header-4.jpg?w=2500&quality=82&strip=all&ssl=1",
                publishedTime = 1_581_193_702_000L
            ),
            Story(
                id = 7,
                source = "DEV Community",
                title = "Binding Android UI with Kotlin Flow",
                author = "Yang",
                description = "Modern Android codebases are becoming increasingly reactive. With concepts and patterns such as MVI, Redux, Unidirectional Data Flow, many components of the system are being modelled as streams.",
                url = "https://dev.to/ychescale9/binding-android-ui-with-kotlin-flow-22ok",
                imageUrl = null,
                publishedTime = 1_581_192_303_000L
            )
        )

        val items = mutableListOf<AdapterItem<Story, FeedType, Unit>>().apply {
            add(AdapterItem.Header(FeedType.TopHeadlines))
            addAll(headlines.map { AdapterItem.Content(it) })
            add(AdapterItem.Footer(Unit))
            add(AdapterItem.Header(FeedType.ForYou))
            addAll(forYou.map { AdapterItem.Content(it) })
        }

        emit(items)
    }
        .mapLatest { HomeState.Idle(it) }
        .catch { Timber.e(it, "flow cancelled") }
        .asLiveData()

    fun refreshHomeFeeds() {
        // TODO
    }
}

// TODO move to HomeStateMachine
sealed class HomeState {
    abstract val items: List<AdapterItem<Story, FeedType, Unit>>

    data class Idle(override val items: List<AdapterItem<Story, FeedType, Unit>>) : HomeState()
    data class InFlight(override val items: List<AdapterItem<Story, FeedType, Unit>>) : HomeState()
    data class Error(override val items: List<AdapterItem<Story, FeedType, Unit>>) : HomeState()
}
