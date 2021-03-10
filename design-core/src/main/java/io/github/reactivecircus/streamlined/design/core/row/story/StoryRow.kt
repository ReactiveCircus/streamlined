package io.github.reactivecircus.streamlined.design.core.row.story

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.BookmarkBorder
import androidx.compose.material.icons.twotone.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun StoryRow(
    storySource: String,
    storyTitle: String,
    publishedTime: String,
    onClick: () -> Unit,
    onBookmarkButtonClick: () -> Unit,
    onReadMoreButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    storyImageContent: (@Composable () -> Unit)? = null,
) {
    Surface(
        contentColor = MaterialTheme.colors.onSurface,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        Text(
                            text = storySource,
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = storyTitle,
                        style = MaterialTheme.typography.h6,
                    )
                }
                if (storyImageContent != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 8.dp)
                            .requiredSize(96.dp)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        storyImageContent()
                    }
                }
            }
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = publishedTime,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onBookmarkButtonClick,
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colors.secondary,
                    )
                }
                IconButton(
                    onClick = onReadMoreButtonClick,
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.MoreHoriz,
                        contentDescription = "Read more",
                        tint = MaterialTheme.colors.secondary,
                    )
                }
            }
        }
    }
}

@Preview("light")
@Composable
private fun PreviewStoryRowLight() {
    StreamlinedTheme(darkTheme = false) {
        PreviewStoryRow()
    }
}

@Preview("dark")
@Composable
private fun PreviewStoryRowDark() {
    StreamlinedTheme(darkTheme = true) {
        PreviewStoryRow()
    }
}

@Composable
private fun PreviewStoryRow() {
    StoryRow(
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
                    .fillMaxSize()
            )
        },
    )
}
