package io.github.reactivecircus.streamlined.navigator.input

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryDetailsInput(
    val storyId: Long
) : Parcelable
