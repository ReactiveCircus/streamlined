@file:Suppress("MaximumLineLength", "MaxLineLength")

package io.github.reactivecircus.streamlined.remote

import io.github.reactivecircus.streamlined.remote.dto.SourceDTO
import io.github.reactivecircus.streamlined.remote.dto.StoryDTO

internal object MockData {
    val mockHeadlineStories = listOf(
        StoryDTO(
            source = SourceDTO("dev.to"),
            author = "Yang",
            title = "Testing Kotlin Lambda Invocations without Mocking",
            description = "In this post I want to share a simple technique for verifying the invocation of a Kotlin lambda function without mocking it.",
            url = "https://dev.to/ychescale9/testing-kotlin-lambda-invocations-without-mocking-3n32",
            urlToImage = null,
            publishedAt = "2020-02-08T20:30:02Z"
        ),
        StoryDTO(
            source = SourceDTO("dev.to"),
            author = "Yang",
            title = "Exploring Cirrus CI for Android",
            description = "I recently wrote about my experience using various cloud-based CI services for running Android instrumented tests on CI for opensource projects, and how I landed on a solution using a custom GitHub Action.",
            url = "https://dev.to/ychescale9/exploring-cirrus-ci-for-android-434",
            urlToImage = null,
            publishedAt = "2020-02-08T19:20:00Z"
        ),
        StoryDTO(
            source = SourceDTO("dev.to"),
            author = "Yang",
            title = "Running Android Instrumented Tests on CI - from Bitrise.io to GitHub Actions",
            description = "It's almost 2020 and it remains a challenge to run Android Instrumented tests on CI, especially for opensource projects and small teams.",
            url = "https://dev.to/ychescale9/running-android-emulators-on-ci-from-bitrise-io-to-github-actions-3j76",
            urlToImage = null,
            publishedAt = "2020-02-08T17:10:00Z"
        ),
        StoryDTO(
            source = SourceDTO("dev.to"),
            author = "Yang",
            title = "Binding Android UI with Kotlin Flow",
            description = "Modern Android codebases are becoming increasingly reactive. With concepts and patterns such as MVI, Redux, Unidirectional Data Flow, many components of the system are being modelled as streams.",
            url = "https://dev.to/ychescale9/binding-android-ui-with-kotlin-flow-22ok",
            urlToImage = null,
            publishedAt = "2020-02-08T17:05:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = null,
            title = "Suspendisse faucibus interdum posuere lorem.",
            description = "Eu nisl nunc mi ipsum faucibus.",
            url = "a.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T17:03:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = null,
            title = "Dignissim cras tincidunt lobortis feugiat.",
            description = "Malesuada proin libero nunc consequat interdum varius.",
            url = "b.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T16:50:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = null,
            title = "Nulla facilisi cras fermentum odio eu.",
            description = "Vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt ornare massa.",
            url = "c.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T16:30:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = null,
            title = "Adipiscing commodo elit at imperdiet.",
            description = "Vestibulum morbi blandit cursus risus.",
            url = "d.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T16:20:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = null,
            title = "Viverra maecenas accumsan lacus vel facilisis volutpat est velit.",
            description = "Malesuada bibendum arcu vitae elementum curabitur vitae.",
            url = "e.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T16:15:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = null,
            title = "Sed faucibus turpis in eu.",
            description = "Ornare massa eget egestas purus viverra accumsan in.",
            url = "f.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T16:10:00Z"
        )
    )

    val mockAllStories = listOf(
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Jennifer Jackson",
            title = "Et egestas quis ipsum suspendisse ultrices gravida.",
            description = "Lacus laoreet non curabitur gravida arcu.",
            url = "1.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T15:15:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Bessie Carlson",
            title = "Vestibulum lorem sed risus ultricies tristique nulla.",
            description = "Arcu risus quis varius quam quisque id diam vel quam.",
            url = "2.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T15:10:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Jack Mcdonald",
            title = "Nunc eget lorem dolor sed viverra ipsum nunc aliquet.",
            description = "Risus quis varius quam quisque id. Aenean et tortor at risus.",
            url = "3.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-08T15:05:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Catherine Shaw",
            title = "Ullamcorper dignissim cras tincidunt lobortis feugiat.",
            description = "Viverra orci sagittis eu volutpat odio facilisis.",
            url = "4.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-07T20:10:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Hugo Reed",
            title = "Arcu vitae elementum curabitur vitae.",
            description = "Viverra justo nec ultrices dui sapien eget mi.",
            url = "5.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-07T19:10:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Andrew Lane",
            title = "Sapien nec sagittis aliquam malesuada.",
            description = "Sed nisi lacus sed viverra tellus.",
            url = "6.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-07T16:10:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Glen Graham",
            title = "Elit at imperdiet dui accumsan sit amet nulla facilisi.",
            description = "Euismod elementum nisi quis eleifend quam adipiscing.",
            url = "7.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-07T11:30:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Floyd Payne",
            title = "Iaculis eu non diam phasellus vestibulum lorem sed risus ultricies.",
            description = "Justo donec enim diam vulputate ut pharetra sit amet.",
            url = "8.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-07T11:10:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Marcella Terry",
            title = "Nisl nisi scelerisque eu ultrices vitae auctor eu augue ut.",
            description = "Interdum consectetur libero id faucibus.",
            url = "9.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-07T03:30:00Z"
        ),
        StoryDTO(
            source = SourceDTO("mock-source"),
            author = "Josephine DrakeTerry",
            title = "Sem viverra aliquet eget sit amet tellus cras adipiscing enim.",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            url = "10.mockarticles.com",
            urlToImage = null,
            publishedAt = "2020-02-06T13:20:00Z"
        )
    )
}
