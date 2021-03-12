package io.github.reactivecircus.streamlined.design.core.row.story

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import io.github.reactivecircus.streamlined.design.core.BookmarkToggleButton
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun MainStoryRow(
    storySource: String,
    storyTitle: String,
    publishedTime: String,
    storyBookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkButtonCheckedChange: (Boolean) -> Unit,
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
            if (storyImageContent != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    storyImageContent()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    text = storySource,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = storyTitle,
                style = MaterialTheme.typography.h6,
            )
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = publishedTime,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                BookmarkToggleButton(
                    checked = storyBookmarked,
                    onCheckedChange = onBookmarkButtonCheckedChange,
                )
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
fun PreviewMainStoryRowLight() {
    StreamlinedTheme(darkTheme = false) {
        PreviewMainStoryRow()
    }
}

@Preview("dark")
@Composable
fun PreviewMainStoryRowDark() {
    StreamlinedTheme(darkTheme = true) {
        PreviewMainStoryRow()
    }
}

@Composable
private fun PreviewMainStoryRow() {
    MainStoryRow(
        storySource = "dev.to",
        storyTitle = "Testing Kotlin Lambda Invocations without Mocking",
        publishedTime = "2 hours ago",
        storyBookmarked = true,
        onClick = {},
        onBookmarkButtonCheckedChange = {},
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
