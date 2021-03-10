package io.github.reactivecircus.streamlined.ui.headlines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.reactivecircus.streamlined.design.core.row.ButtonRow
import io.github.reactivecircus.streamlined.design.core.row.EmptyPlaceholderRow
import io.github.reactivecircus.streamlined.design.core.row.RowDivider
import io.github.reactivecircus.streamlined.design.core.row.SectionDivider
import io.github.reactivecircus.streamlined.design.core.row.SectionHeaderRow
import io.github.reactivecircus.streamlined.design.core.row.story.MainStoryRow
import io.github.reactivecircus.streamlined.design.core.row.story.StoryRow

@Composable
fun HeadlinesScreen() {
    StreamlinedTheme {
        ProvideWindowInsets {
            Scaffold(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(bottom = false),
                topBar = {
                    TopBar(
                        title = stringResource(R.string.title_headlines)
                    )
                },
            ) {
                HeadlinesContent(
                    Modifier.background(MaterialTheme.colors.background)
                )
            }
        }
    }
}

@Composable
fun HeadlinesContent(
    modifier: Modifier = Modifier,
) {
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center,
//    ) {
//        Text(
//            text = "Headlines",
//            modifier = modifier.padding(24.dp),
//            textAlign = TextAlign.Center,
//        )
//    }
    LazyColumn(modifier = modifier) {
        item {
            SectionHeaderRow(title = "Top Headlines")
        }
        item {
            MainStoryRow(
                storySource = "dev.to",
                storyTitle = "Testing Kotlin Lambda Invocations without Mocking",
                publishedTime = "2 hours ago",
                onClick = {},
                onBookmarkButtonClick = {},
                onReadMoreButtonClick = {},
                storyImageContent = {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                            )
                            .fillMaxWidth()
                            .aspectRatio(2f)
                    )
                },
            )
        }
        item {
            RowDivider()
        }
        item {
            StoryRow(
                storySource = "dev.to",
                storyTitle = "Exploring Cirrus CI for Android",
                publishedTime = "2 days ago",
                onClick = {},
                onBookmarkButtonClick = {},
                onReadMoreButtonClick = {},
                storyImageContent = {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                            )
                            .fillMaxSize()
                    )
                },
            )
        }
        item {
            RowDivider()
        }
        item {
            ButtonRow(onClick = {}, buttonLabel = "Read more headlines")
        }
        item {
            SectionDivider()
        }
        item {
            SectionHeaderRow(title = "For You")
        }
        item {
            MainStoryRow(
                storySource = "Android Developers Blog",
                storyTitle = "Android Dev Challenge: lift off with Jetpack Compose",
                publishedTime = "3 days ago",
                onClick = {},
                onBookmarkButtonClick = {},
                onReadMoreButtonClick = {},
                storyImageContent = {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                            )
                            .fillMaxWidth()
                            .aspectRatio(2f)
                    )
                },
            )
        }
        item {
            RowDivider()
        }
        item {
            StoryRow(
                storySource = "Android Developers Blog",
                storyTitle = "First preview of Android 12",
                publishedTime = "2 weeks ago",
                onClick = {},
                onBookmarkButtonClick = {},
                onReadMoreButtonClick = {},
                storyImageContent = {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                            )
                            .fillMaxSize()
                    )
                },
            )
        }
        item {
            SectionDivider()
        }
        item {
            SectionHeaderRow(title = "Trending")
        }
        item {
            EmptyPlaceholderRow(emptyMessage = "No trending stories found.")
        }
    }
}

@Preview
@Composable
private fun PreviewHeadlinesScreen() {
    HeadlinesScreen()
}

// TODO move to :ui-home
@Preview("light", heightDp = 1400)
@Composable
private fun PreviewHomeFeedsLight() {
    StreamlinedTheme(darkTheme = false) {
        PreviewHomeFeeds()
    }
}

// TODO move to :ui-home
@Preview("dark", heightDp = 1400)
@Composable
private fun PreviewHomeFeedsDark() {
    StreamlinedTheme(darkTheme = true) {
        PreviewHomeFeeds()
    }
}

// TODO move to :ui-home
@Composable
private fun PreviewHomeFeeds() {
    Column(
        modifier = Modifier.background(MaterialTheme.colors.surface)
    ) {
        SectionHeaderRow(title = "Top Headlines")
        MainStoryRow(
            storySource = "dev.to",
            storyTitle = "Testing Kotlin Lambda Invocations without Mocking",
            publishedTime = "2 hours ago",
            onClick = {},
            onBookmarkButtonClick = {},
            onReadMoreButtonClick = {},
            storyImageContent = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                        )
                        .fillMaxWidth()
                        .aspectRatio(2f)
                )
            },
        )
        RowDivider()
        StoryRow(
            storySource = "dev.to",
            storyTitle = "Exploring Cirrus CI for Android",
            publishedTime = "2 days ago",
            onClick = {},
            onBookmarkButtonClick = {},
            onReadMoreButtonClick = {},
            storyImageContent = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                        )
                        .fillMaxSize()
                )
            },
        )
        RowDivider()
        ButtonRow(onClick = {}, buttonLabel = "Read more headlines")
        SectionDivider()
        SectionHeaderRow(title = "For You")
        MainStoryRow(
            storySource = "Android Developers Blog",
            storyTitle = "Android Dev Challenge: lift off with Jetpack Compose",
            publishedTime = "3 days ago",
            onClick = {},
            onBookmarkButtonClick = {},
            onReadMoreButtonClick = {},
            storyImageContent = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                        )
                        .fillMaxWidth()
                        .aspectRatio(2f)
                )
            },
        )
        RowDivider()
        StoryRow(
            storySource = "Android Developers Blog",
            storyTitle = "First preview of Android 12",
            publishedTime = "2 weeks ago",
            onClick = {},
            onBookmarkButtonClick = {},
            onReadMoreButtonClick = {},
            storyImageContent = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
                        )
                        .fillMaxSize()
                )
            },
        )
        SectionDivider()
        SectionHeaderRow(title = "Trending")
        EmptyPlaceholderRow(emptyMessage = "No trending stories found.")
    }
}
