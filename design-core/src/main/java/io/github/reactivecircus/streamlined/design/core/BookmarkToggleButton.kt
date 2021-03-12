package io.github.reactivecircus.streamlined.design.core

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.BookmarkAdded
import androidx.compose.material.icons.twotone.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun BookmarkToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.size(40.dp),
    ) {
        val icon = if (!checked) {
            Icons.TwoTone.BookmarkBorder
        } else {
            Icons.TwoTone.BookmarkAdded
        }
        val contentDescription = if (!checked) {
            "Add to reading list"
        } else {
            "Remove from reading list"
        }
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.secondary,
        )
    }
}

@Preview("unchecked, light")
@Composable
fun PreviewBookmarkToggleButtonUncheckedLight() {
    StreamlinedTheme(darkTheme = false) {
        Surface {
            BookmarkToggleButton(
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}

@Preview("unchecked, dark")
@Composable
fun PreviewBookmarkToggleButtonUncheckedDark() {
    StreamlinedTheme(darkTheme = true) {
        Surface {
            BookmarkToggleButton(
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}

@Preview("checked, light")
@Composable
fun PreviewBookmarkToggleButtonCheckedLight() {
    StreamlinedTheme(darkTheme = false) {
        Surface {
            BookmarkToggleButton(
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}

@Preview("checked, dark")
@Composable
fun PreviewBookmarkToggleButtonCheckedDark() {
    StreamlinedTheme(darkTheme = true) {
        Surface {
            BookmarkToggleButton(
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}
