package io.github.reactivecircus.streamlined.ui.common.util

import androidx.recyclerview.widget.RecyclerView
import reactivecircus.blueprint.ui.extension.isAnimationOn

/**
 * Sets the RecyclerView's `itemAnimator` to null if animation is turned off on the device.
 */
fun RecyclerView.disableItemAnimatorIfTurnedOffGlobally() {
    if (!context.isAnimationOn) {
        itemAnimator = null
    }
}
