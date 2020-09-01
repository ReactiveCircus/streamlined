@file:Suppress("ComplexMethod")

package io.github.reactivecircus.streamlined.design.foundation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * An IconSwitch is a two state toggleable component with an icon inside the thumb that provides on/off like options.
 * This is similar to [androidx.compose.material.Switch] with 2 differences:
 *  1. The provided [content] is drawn inside the circular thumb.
 *  2. A border is added around the track.
 *
 * @param checked whether or not this components is checked
 * @param onCheckedChange callback to be invoked when IconSwitch is being clicked,
 * therefore the change of checked state is requested.
 * @param modifier Modifier to be applied to the switch layout
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 * @param colors [IconSwitchColors] that will be used to determine the color of the thumb and track
 * in different states. See [IconSwitchConstants.defaultColors].
 * @param sizes [IconSwitchSizes] that will be used to determine the sizes of the thumb, track and border.
 * See [IconSwitchConstants.defaultSizes].
 * @param content the content (icon) to be drawn inside the IconSwitch. This is typically an [Icon].
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IconSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: IconSwitchColors = IconSwitchConstants.defaultColors(),
    sizes: IconSwitchSizes = IconSwitchConstants.defaultSizes(),
    content: @Composable () -> Unit
) {
    val minBound = 0f
    val maxBound = with(LocalDensity.current) { (sizes.trackWidth - sizes.thumbDiameter).toPx() }
    val swipeableState = rememberSwipeableStateFor(checked, onCheckedChange ?: {}, AnimationSpec)
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val toggleableModifier = if (onCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            onValueChange = onCheckedChange,
            enabled = enabled,
            role = Role.Switch,
            interactionSource = interactionSource,
            indication = null
        )
    } else {
        Modifier
    }

    Box(
        modifier
            .then(toggleableModifier)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(minBound to false, maxBound to true),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal,
                enabled = enabled,
                reverseDirection = isRtl,
                interactionSource = interactionSource,
                resistance = null
            )
            .wrapContentSize(Alignment.Center)
            .padding(DefaultSwitchPadding)
            .size(sizes.trackWidth, sizes.trackHeight)
    ) {
        SwitchImpl(
            checked = checked,
            enabled = enabled,
            colors = colors,
            sizes = sizes,
            thumbValue = swipeableState.offset,
            interactionSource = interactionSource,
            content = content,
        )
    }
}

/**
 * Represents the colors used by a [IconSwitch] in different states
 *
 * See [IconSwitchConstants.defaultColors] for the default implementation that follows Material
 * specifications.
 */
@ExperimentalMaterialApi
@Stable
interface IconSwitchColors {

    /**
     * Represents the color used for the icon switch's thumb, depending on [enabled] and [checked].
     *
     * @param enabled whether the [IconSwitch] is enabled or not
     * @param checked whether the [IconSwitch] is checked or not
     */
    fun thumbColor(enabled: Boolean, checked: Boolean): Color

    /**
     * Represents the color used for the icon switch's track, depending on [enabled] and [checked].
     *
     * @param enabled whether the [IconSwitch] is enabled or not
     * @param checked whether the [IconSwitch] is checked or not
     */
    fun trackColor(enabled: Boolean, checked: Boolean): Color

    /**
     * Represents the color used for the icon switch's track border, depending on [enabled] and [checked].
     *
     * @param enabled whether the [IconSwitch] is enabled or not
     * @param checked whether the [IconSwitch] is checked or not
     */
    fun trackBorderColor(enabled: Boolean, checked: Boolean): Color
}

/**
 * Represents the sizes used by a [IconSwitch]
 *
 * See [IconSwitchConstants.defaultSizes] for the default implementation.
 */
@ExperimentalMaterialApi
@Stable
interface IconSwitchSizes {

    /**
     * Represents the width of the icon switch's track.
     */
    val trackWidth: Dp

    /**
     * Represents the height of the icon switch's track.
     */
    val trackHeight: Dp

    /**
     * Represents the diameter size of the icon switch's thumb.
     */
    val thumbDiameter: Dp

    /**
     * Represents the border size of the icon switch's track.
     */
    val trackBorderSize: Dp
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.SwitchImpl(
    checked: Boolean,
    enabled: Boolean,
    colors: IconSwitchColors,
    sizes: IconSwitchSizes,
    thumbValue: State<Float>,
    interactionSource: InteractionSource,
    content: @Composable () -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDragged by interactionSource.collectIsDraggedAsState()
    val hasInteraction = isPressed || isDragged
    val elevation = if (hasInteraction) {
        ThumbPressedElevation
    } else {
        ThumbDefaultElevation
    }
    val trackColor = colors.trackColor(enabled, checked)
    val borderColor = colors.trackBorderColor(enabled, checked)
    Canvas(
        Modifier
            .align(Alignment.Center)
            .fillMaxSize()
    ) {
        drawTrack(
            trackColor = trackColor,
            trackWidth = sizes.trackWidth.toPx(),
            trackHeight = sizes.trackHeight.toPx(),
            trackBorderColor = borderColor,
            trackBorderSize = sizes.trackBorderSize.toPx(),
        )
    }
    val thumbColor = colors.thumbColor(enabled, checked)
    Surface(
        shape = CircleShape,
        color = thumbColor,
        elevation = elevation,
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset { IntOffset(thumbValue.value.roundToInt(), 0) }
            .indication(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = ThumbRippleRadius)
            )
            .requiredSize(sizes.thumbDiameter),
        content = content
    )
}

private fun DrawScope.drawTrack(
    trackColor: Color,
    trackWidth: Float,
    trackHeight: Float,
    trackBorderColor: Color,
    trackBorderSize: Float,
) {
    val trackRadius = trackHeight / 2

    // border
    drawLine(
        trackBorderColor,
        Offset(trackRadius, center.y),
        Offset(trackWidth - trackRadius, center.y),
        trackHeight,
        StrokeCap.Round
    )

    // fill
    drawLine(
        trackColor,
        Offset(trackRadius, center.y),
        Offset(trackWidth - trackRadius, center.y),
        trackHeight - trackBorderSize,
        StrokeCap.Round
    )
}

/**
 * Copied from [androidx.compose.material.rememberSwipeableStateFor] which is internal.
 */
@Composable
@ExperimentalMaterialApi
private fun <T : Any> rememberSwipeableStateFor(
    value: T,
    onValueChange: (T) -> Unit,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): SwipeableState<T> {
    val swipeableState = rememberSwipeableState(
        initialValue = value,
        animationSpec = animationSpec
    )
    val forceAnimationCheck = remember { mutableStateOf(false) }
    LaunchedEffect(value, forceAnimationCheck.value) {
        if (value != swipeableState.currentValue) {
            swipeableState.animateTo(value)
        }
    }
    DisposableEffect(swipeableState.currentValue) {
        if (value != swipeableState.currentValue) {
            onValueChange(swipeableState.currentValue)
            forceAnimationCheck.value = !forceAnimationCheck.value
        }
        onDispose { }
    }
    return swipeableState
}

private val TrackWidth = 48.dp
private val TrackHeight = 24.dp
private val ThumbDiameter = 32.dp
private val TrackBorderSize = 6.dp

private val ThumbRippleRadius = 24.dp

private val DefaultSwitchPadding = 2.dp

private val AnimationSpec = TweenSpec<Float>(durationMillis = 100)

private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp

/**
 * Contains the default values used by [IconSwitch]
 */
object IconSwitchConstants {
    /**
     * Creates a [IconSwitchColors] that represents the different colors used in a [IconSwitch] in
     * different states.
     *
     * @param checkedThumbColor the color used for the thumb when enabled and checked
     * @param checkedTrackColor the color used for the track when enabled and checked
     * @param checkedTrackAlpha the alpha applied to [checkedTrackColor] and
     * [disabledCheckedTrackColor]
     * @param checkedBorderColor the color used for the track border when enabled and checked
     * @param checkedBorderAlpha the alpha applied to [checkedBorderColor] and
     * [disabledCheckedBorderColor]
     * @param uncheckedThumbColor the color used for the thumb when enabled and unchecked
     * @param uncheckedTrackColor the color used for the track when enabled and unchecked
     * @param uncheckedTrackAlpha the alpha applied to [uncheckedTrackColor] and
     * [disabledUncheckedTrackColor]
     * @param uncheckedBorderColor the color used for the track when enabled and unchecked
     * @param uncheckedBorderAlpha the alpha applied to [uncheckedBorderColor] and
     * [disabledUncheckedTrackColor]
     * @param disabledCheckedThumbColor the color used for the thumb when disabled and checked
     * @param disabledCheckedTrackColor the color used for the track when disabled and checked
     * @param disabledCheckedBorderColor the color used for the track border when disabled and checked
     * @param disabledUncheckedThumbColor the color used for the thumb when disabled and unchecked
     * @param disabledUncheckedTrackColor the color used for the track when disabled and unchecked
     * @param disabledUncheckedBorderColor the color used for the track border when disabled and unchecked
     */
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun defaultColors(
        checkedThumbColor: Color = MaterialTheme.colors.secondaryVariant,
        checkedTrackColor: Color = checkedThumbColor,
        checkedTrackAlpha: Float = 0.54f,
        checkedBorderColor: Color = checkedTrackColor,
        checkedBorderAlpha: Float = 0.75f,
        uncheckedThumbColor: Color = MaterialTheme.colors.surface,
        uncheckedTrackColor: Color = MaterialTheme.colors.onSurface,
        uncheckedTrackAlpha: Float = 0.38f,
        uncheckedBorderColor: Color = MaterialTheme.colors.onSurface,
        uncheckedBorderAlpha: Float = 0.45f,
        disabledCheckedThumbColor: Color = checkedThumbColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(MaterialTheme.colors.surface),
        disabledCheckedTrackColor: Color = checkedTrackColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(MaterialTheme.colors.surface),
        disabledCheckedBorderColor: Color = checkedBorderColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(MaterialTheme.colors.surface),
        disabledUncheckedThumbColor: Color = uncheckedThumbColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(MaterialTheme.colors.surface),
        disabledUncheckedTrackColor: Color = uncheckedTrackColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(MaterialTheme.colors.surface),
        disabledUncheckedBorderColor: Color = uncheckedBorderColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(MaterialTheme.colors.surface),
    ): IconSwitchColors = DefaultIconSwitchColors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor.copy(alpha = checkedTrackAlpha),
        checkedBorderColor = checkedBorderColor.copy(alpha = checkedBorderAlpha),
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor.copy(alpha = uncheckedTrackAlpha),
        uncheckedBorderColor = uncheckedBorderColor.copy(alpha = uncheckedBorderAlpha),
        disabledCheckedThumbColor = disabledCheckedThumbColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor.copy(alpha = checkedTrackAlpha),
        disabledCheckedBorderColor = disabledCheckedBorderColor.copy(alpha = checkedBorderAlpha),
        disabledUncheckedThumbColor = disabledUncheckedThumbColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor.copy(alpha = uncheckedTrackAlpha),
        disabledUncheckedBorderColor = disabledUncheckedBorderColor.copy(alpha = uncheckedBorderAlpha),
    )

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun defaultSizes(
        trackWidth: Dp = TrackWidth,
        trackHeight: Dp = TrackHeight,
        thumbDiameter: Dp = ThumbDiameter,
        trackBorderSize: Dp = TrackBorderSize,
    ): IconSwitchSizes = DefaultIconSwitchSizes(
        trackWidth = trackWidth,
        trackHeight = trackHeight,
        thumbDiameter = thumbDiameter,
        trackBorderSize = trackBorderSize,
    )
}

/**
 * Default [IconSwitchColors] implementation.
 */
@OptIn(ExperimentalMaterialApi::class)
@Immutable
private class DefaultIconSwitchColors(
    private val checkedThumbColor: Color,
    private val checkedTrackColor: Color,
    private val checkedBorderColor: Color,
    private val uncheckedThumbColor: Color,
    private val uncheckedTrackColor: Color,
    private val uncheckedBorderColor: Color,
    private val disabledCheckedThumbColor: Color,
    private val disabledCheckedTrackColor: Color,
    private val disabledCheckedBorderColor: Color,
    private val disabledUncheckedThumbColor: Color,
    private val disabledUncheckedTrackColor: Color,
    private val disabledUncheckedBorderColor: Color,
) : IconSwitchColors {
    override fun thumbColor(enabled: Boolean, checked: Boolean): Color {
        return if (enabled) {
            if (checked) checkedThumbColor else uncheckedThumbColor
        } else {
            if (checked) disabledCheckedThumbColor else disabledUncheckedThumbColor
        }
    }

    override fun trackColor(enabled: Boolean, checked: Boolean): Color {
        return if (enabled) {
            if (checked) checkedTrackColor else uncheckedTrackColor
        } else {
            if (checked) disabledCheckedTrackColor else disabledUncheckedTrackColor
        }
    }

    override fun trackBorderColor(enabled: Boolean, checked: Boolean): Color {
        return if (enabled) {
            if (checked) checkedBorderColor else uncheckedBorderColor
        } else {
            if (checked) disabledCheckedBorderColor else disabledUncheckedBorderColor
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultIconSwitchColors

        if (checkedThumbColor != other.checkedThumbColor) return false
        if (checkedTrackColor != other.checkedTrackColor) return false
        if (checkedBorderColor != other.checkedBorderColor) return false
        if (uncheckedThumbColor != other.uncheckedThumbColor) return false
        if (uncheckedTrackColor != other.uncheckedTrackColor) return false
        if (uncheckedBorderColor != other.uncheckedBorderColor) return false
        if (disabledCheckedThumbColor != other.disabledCheckedThumbColor) return false
        if (disabledCheckedTrackColor != other.disabledCheckedTrackColor) return false
        if (disabledCheckedBorderColor != other.disabledCheckedBorderColor) return false
        if (disabledUncheckedThumbColor != other.disabledUncheckedThumbColor) return false
        if (disabledUncheckedTrackColor != other.disabledUncheckedTrackColor) return false
        if (disabledUncheckedBorderColor != other.disabledUncheckedBorderColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checkedThumbColor.hashCode()
        result = 31 * result + checkedTrackColor.hashCode()
        result = 31 * result + checkedBorderColor.hashCode()
        result = 31 * result + uncheckedThumbColor.hashCode()
        result = 31 * result + uncheckedTrackColor.hashCode()
        result = 31 * result + uncheckedBorderColor.hashCode()
        result = 31 * result + disabledCheckedThumbColor.hashCode()
        result = 31 * result + disabledCheckedTrackColor.hashCode()
        result = 31 * result + disabledCheckedBorderColor.hashCode()
        result = 31 * result + disabledUncheckedThumbColor.hashCode()
        result = 31 * result + disabledUncheckedTrackColor.hashCode()
        result = 31 * result + disabledUncheckedBorderColor.hashCode()
        return result
    }
}

/**
 * Default [IconSwitchSizes] implementation.
 */
@OptIn(ExperimentalMaterialApi::class)
@Immutable
private class DefaultIconSwitchSizes(
    override val trackWidth: Dp,
    override val trackHeight: Dp,
    override val thumbDiameter: Dp,
    override val trackBorderSize: Dp,
) : IconSwitchSizes {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultIconSwitchSizes

        if (trackWidth != other.trackWidth) return false
        if (trackHeight != other.trackHeight) return false
        if (thumbDiameter != other.thumbDiameter) return false
        if (trackBorderSize != other.trackBorderSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trackWidth.hashCode()
        result = 31 * result + trackHeight.hashCode()
        result = 31 * result + thumbDiameter.hashCode()
        result = 31 * result + trackBorderSize.hashCode()
        return result
    }
}
