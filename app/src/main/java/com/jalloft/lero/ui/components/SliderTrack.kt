package com.jalloft.lero.ui.components

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
fun SliderState.toRangeSliderState() = RangeSliderState(activeRangeStart, activeRangeEnd, steps, onValueChangeFinished, valueRange)


@OptIn(ExperimentalMaterial3Api::class)
fun RangeSliderState.toSliderState() = SliderState(activeRangeStart, activeRangeEnd, steps, onValueChangeFinished, valueRange)

@Composable
fun SliderTrack(
    modifier: Modifier = Modifier,
    sliderState: SliderState,
    colors: SliderTrackColors = SliderTrackDefaults.colors(),
    enabled: Boolean = true
) {
    val inactiveTrackColor = colors.trackColor(enabled, active = false)
    val activeTrackColor = colors.trackColor(enabled, active = true)
    val inactiveTickColor = colors.tickColor(enabled, active = false)
    val activeTickColor = colors.tickColor(enabled, active = true)

    Canvas(
        modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        drawSliderTrack(
            sliderState.tickFractions,
            sliderState.coercedActiveRangeStartAsFraction,
            sliderState.coercedActiveRangeEndAsFraction,
            inactiveTrackColor.value,
            activeTrackColor.value,
            inactiveTickColor.value,
            activeTickColor.value
        )
    }
}

fun DrawScope.drawSliderTrack(
    tickFractions: FloatArray,
    activeRangeStart: Float,
    activeRangeEnd: Float,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    inactiveTickColor: Color,
    activeTickColor: Color
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val tickSize = 2.0.dp.toPx()
    val trackStrokeWidth = 4.0.dp.toPx()
    drawLine(
        inactiveTrackColor,
        sliderStart,
        sliderEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )
    val sliderValueEnd = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeEnd,
        center.y
    )

    val sliderValueStart = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeStart,
        center.y
    )

    drawLine(
        activeTrackColor,
        sliderValueStart,
        sliderValueEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )

    for (tick in tickFractions) {
        val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
        drawCircle(
            color = if (outsideFraction) inactiveTickColor else activeTickColor,
            center = Offset(lerp(sliderStart, sliderEnd, tick).x, center.y),
            radius = tickSize / 2f
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SliderTrack(
//    sliderPositions: RangeSliderState,
//    modifier: Modifier = Modifier,
//    colors: SliderTrackColors = SliderTrackDefaults.colors(),
//    enabled: Boolean = true,
//){
//    val inactiveTrackColor = colors.trackColor(enabled, active = false)
//    val activeTrackColor = colors.trackColor(enabled, active = true)
//    val inactiveTickColor = colors.tickColor(enabled, active = false)
//    val activeTickColor = colors.tickColor(enabled, active = true)
//    val activeTickTextColor = colors.tickTextColor(enabled, active = true)
//
//    val textMeasurer = rememberTextMeasurer()
//
//    Canvas(
//        modifier
//            .fillMaxWidth()
//            .height(4.dp)
//    ) {
//        val isRtl = layoutDirection == LayoutDirection.Rtl
//        val sliderLeft = Offset(0f, center.y)
//        val sliderRight = Offset(size.width, center.y)
//        val sliderStart = if (isRtl) sliderRight else sliderLeft
//        val sliderEnd = if (isRtl) sliderLeft else sliderRight
//        val tickSize = 3.dp.toPx()
//        val trackStrokeWidth = 4.dp.toPx()
//        drawLine(
//            inactiveTrackColor.value,
//            sliderStart,
//            sliderEnd,
//            trackStrokeWidth,
//            StrokeCap.Round
//        )
//        val sliderValueEnd = Offset(
//            sliderStart.x +
//                    (sliderEnd.x - sliderStart.x) * sliderPositions.activeRange.endInclusive,
//            center.y
//        )
//
//        val sliderValueStart = Offset(
//            sliderStart.x +
//                    (sliderEnd.x - sliderStart.x) * sliderPositions.activeRange.start,
//            center.y
//        )
//
//        drawLine(
//            activeTrackColor.value,
//            sliderValueStart,
//            sliderValueEnd,
//            trackStrokeWidth,
//            StrokeCap.Round
//        )
//
//        sliderPositions.tickFractions.groupBy {
//            it > sliderPositions.activeRange.endInclusive ||
//                    it < sliderPositions.activeRange.start
//        }.forEach { (outsideFraction, list) ->
//            val offsetList =  list.map { Offset(lerp(sliderStart, sliderEnd, it).x, center.y) }
//            drawPoints(
//                offsetList,
//                PointMode.Points,
//                (if (outsideFraction) inactiveTickColor else activeTickColor).value,
//                tickSize,
//                StrokeCap.Round
//            )
//        }
//
//        sliderPositions.tickFractions.forEachIndexed { index, tick ->
//            val textPoint = Offset(lerp(sliderStart, sliderEnd, tick).x, center.y)
//            val textLayoutResult = textMeasurer.measure(
//                AnnotatedString("${index + 1}"),
//                style = TextStyle(color = activeTickTextColor.value, fontSize = 10.sp)
//            )
//            drawText(
//                textLayoutResult,
//                topLeft = textPoint.copy(x = textPoint.x - textLayoutResult.size.width.div(2f),y = textPoint.y + textLayoutResult.size.height),
//            )
//        }
//    }
//}


private fun stepsToTickFractions(steps: Int): FloatArray {
    return if (steps == 0) floatArrayOf() else FloatArray(steps + 2) { it.toFloat() / (steps + 1) }
}

private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

@Immutable
class SliderTrackColors internal constructor(
    private val activeTrackColor: Color,
    private val activeTickColor: Color,
    private val activeTickTextColor: Color,
    private val inactiveTrackColor: Color,
    private val inactiveTickColor: Color,
    private val disabledActiveTrackColor: Color,
    private val disabledActiveTickColor: Color,
    private val disabledInactiveTrackColor: Color,
    private val disabledInactiveTickColor: Color
) {

    @Composable
    internal fun trackColor(enabled: Boolean, active: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                if (active) activeTrackColor else inactiveTrackColor
            } else {
                if (active) disabledActiveTrackColor else disabledInactiveTrackColor
            }
        )
    }

    @Composable
    internal fun tickColor(enabled: Boolean, active: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                if (active) activeTickColor else inactiveTickColor
            } else {
                if (active) disabledActiveTickColor else disabledInactiveTickColor
            }
        )
    }

    @Composable
    internal fun tickTextColor(enabled: Boolean, active: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                if (active) activeTickTextColor else inactiveTickColor
            } else {
                if (active) disabledActiveTickColor else disabledInactiveTickColor
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is SliderTrackColors) return false
        if (activeTrackColor != other.activeTrackColor) return false
        if (activeTickColor != other.activeTickColor) return false
        if (inactiveTrackColor != other.inactiveTrackColor) return false
        if (inactiveTickColor != other.inactiveTickColor) return false
        if (disabledActiveTrackColor != other.disabledActiveTrackColor) return false
        if (disabledActiveTickColor != other.disabledActiveTickColor) return false
        if (disabledInactiveTrackColor != other.disabledInactiveTrackColor) return false
        return disabledInactiveTickColor == other.disabledInactiveTickColor
    }

    override fun hashCode(): Int {
        var result = activeTrackColor.hashCode()
        result = 31 * result + activeTickColor.hashCode()
        result = 31 * result + inactiveTrackColor.hashCode()
        result = 31 * result + inactiveTickColor.hashCode()
        result = 31 * result + disabledActiveTrackColor.hashCode()
        result = 31 * result + disabledActiveTickColor.hashCode()
        result = 31 * result + disabledInactiveTrackColor.hashCode()
        result = 31 * result + disabledInactiveTickColor.hashCode()
        return result
    }
}


private fun snapValueToTick(
    current: Float,
    tickFractions: FloatArray,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return tickFractions
        .minByOrNull { abs(androidx.compose.ui.util.lerp(minPx, maxPx, it) - current) }
        ?.run { androidx.compose.ui.util.lerp(minPx, maxPx, this) }
        ?: current
}


private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    androidx.compose.ui.util.lerp(a2, b2, calcFraction(a1, b1, x1))

// Scale x.start, x.endInclusive from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x: SliderRangeTrack, a2: Float, b2: Float) =
    SliderRangeTrack(
        scale(a1, b1, x.start, a2, b2),
       scale(a1, b1, x.endInclusive, a2, b2)
    )

class SliderState(
    activeRangeStart: Float = 0f,
    activeRangeEnd: Float = 1f,
    @IntRange(from = 0)
    val steps: Int = 0,
    val onValueChangeFinished: (() -> Unit)? = null,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    private var activeRangeStartState by mutableFloatStateOf(activeRangeStart)
    private var activeRangeEndState by mutableFloatStateOf(activeRangeEnd)

    /**
     * [Float] that indicates the start of the current active range for the [RangeSlider].
     */
    var activeRangeStart: Float
        set(newVal) {
            val coercedValue = newVal.coerceIn(valueRange.start, activeRangeEnd)
            val snappedValue = snapValueToTick(
                coercedValue,
                tickFractions,
                valueRange.start,
                valueRange.endInclusive
            )
            activeRangeStartState = snappedValue
        }
        get() = activeRangeStartState

    /**
     * [Float] that indicates the end of the current active range for the [RangeSlider].
     */
    var activeRangeEnd: Float
        set(newVal) {
            val coercedValue = newVal.coerceIn(activeRangeStart, valueRange.endInclusive)
            val snappedValue = snapValueToTick(
                coercedValue,
                tickFractions,
                valueRange.start,
                valueRange.endInclusive
            )
            activeRangeEndState = snappedValue
        }
        get() = activeRangeEndState

    internal var onValueChange: ((SliderRangeTrack) -> Unit)? = null

     val tickFractions = stepsToTickFractions(steps)

     var startThumbWidth by mutableFloatStateOf(0f)
     var endThumbWidth by mutableFloatStateOf(0f)
     var totalWidth by mutableIntStateOf(0)
     var rawOffsetStart by mutableFloatStateOf(0f)
     var rawOffsetEnd by mutableFloatStateOf(0f)

    internal var isRtl by mutableStateOf(false)

    internal val gestureEndAction: (Boolean) -> Unit = {
        onValueChangeFinished?.invoke()
    }

    private var maxPx by mutableFloatStateOf(0f)
    private var minPx by mutableFloatStateOf(0f)

    internal fun onDrag(isStart: Boolean, offset: Float) {
        val offsetRange = if (isStart) {
            rawOffsetStart = (rawOffsetStart + offset)
            rawOffsetEnd = scaleToOffset(minPx, maxPx, activeRangeEnd)
            val offsetEnd = rawOffsetEnd
            var offsetStart = rawOffsetStart.coerceIn(minPx, offsetEnd)
            offsetStart = snapValueToTick(offsetStart, tickFractions, minPx, maxPx)
            SliderRangeTrack(offsetStart, offsetEnd)
        } else {
            rawOffsetEnd = (rawOffsetEnd + offset)
            rawOffsetStart = scaleToOffset(minPx, maxPx, activeRangeStart)
            val offsetStart = rawOffsetStart
            var offsetEnd = rawOffsetEnd.coerceIn(offsetStart, maxPx)
            offsetEnd = snapValueToTick(offsetEnd, tickFractions, minPx, maxPx)
            SliderRangeTrack(offsetStart, offsetEnd)
        }
        val scaledUserValue = scaleToUserValue(minPx, maxPx, offsetRange)
        if (scaledUserValue != SliderRangeTrack(activeRangeStart, activeRangeEnd)) {
            if (onValueChange != null) {
                onValueChange?.let { it(scaledUserValue) }
            } else {
                this.activeRangeStart = scaledUserValue.start
                this.activeRangeEnd = scaledUserValue.endInclusive
            }
        }
    }

     val coercedActiveRangeStartAsFraction
        get() = calcFraction(
            valueRange.start,
            valueRange.endInclusive,
            activeRangeStart
        )

     val coercedActiveRangeEndAsFraction
        get() = calcFraction(
            valueRange.start,
            valueRange.endInclusive,
            activeRangeEnd
        )

    internal val startSteps
        get() = floor(steps * coercedActiveRangeEndAsFraction).toInt()

    internal val endSteps
        get() = floor(steps * (1f - coercedActiveRangeStartAsFraction)).toInt()

    // scales range offset from within minPx..maxPx to within valueRange.start..valueRange.end
    private fun scaleToUserValue(
        minPx: Float,
        maxPx: Float,
        offset: SliderRangeTrack
    ) = scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

    // scales float userValue within valueRange.start..valueRange.end to within minPx..maxPx
    private fun scaleToOffset(minPx: Float, maxPx: Float, userValue: Float) =
        scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)

    internal fun updateMinMaxPx() {
        val newMaxPx = max(totalWidth - endThumbWidth / 2, 0f)
        val newMinPx = min(startThumbWidth / 2, newMaxPx)
        if (minPx != newMinPx || maxPx != newMaxPx) {
            minPx = newMinPx
            maxPx = newMaxPx
            rawOffsetStart = scaleToOffset(
                minPx,
                maxPx,
                activeRangeStart
            )
            rawOffsetEnd = scaleToOffset(
                minPx,
                maxPx,
                activeRangeEnd
            )
        }
    }
}

@JvmInline
value class SliderRangeTrack(
    val packedValue: Long
) {
    /**
     * start of the [SliderRange]
     */
    @Stable
    val start: Float
        get() {
            // Explicitly compare against packed values to avoid auto-boxing of Size.Unspecified
            check(this.packedValue != Unspecified.packedValue) {
                "SliderRange is unspecified"
            }
            return unpackFloat1(packedValue)
        }

    /**
     * End (inclusive) of the [SliderRange]
     */
    @Stable
    val endInclusive: Float
        get() {
            // Explicitly compare against packed values to avoid auto-boxing of Size.Unspecified
            check(this.packedValue != Unspecified.packedValue) {
                "SliderRange is unspecified"
            }
            return unpackFloat2(packedValue)
        }

    companion object {
        /**
         * Represents an unspecified [SliderRange] value, usually a replacement for `null`
         * when a primitive value is desired.
         */
        @Stable
        val Unspecified = SliderRangeTrack(Float.NaN, Float.NaN)
    }

    /**
     * String representation of the [SliderRange]
     */
    override fun toString() = if (isSpecified) {
        "$start..$endInclusive"
    } else {
        "FloatRange.Unspecified"
    }
}

val SliderRangeTrack.isSpecified: Boolean get() =
    packedValue != SliderRangeTrack.Unspecified.packedValue

fun SliderRangeTrack(start: Float, endInclusive: Float): SliderRangeTrack {
    val isUnspecified = start.isNaN() && endInclusive.isNaN()
    require(isUnspecified || start <= endInclusive) {
        "start($start) must be <= endInclusive($endInclusive)"
    }
    return SliderRangeTrack(packFloats(start, endInclusive))
}

@Stable
object SliderTrackDefaults {

    @Composable
    fun colors(
        activeTrackColor: Color = MaterialTheme.colorScheme.primary,
        activeTickColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
        inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        inactiveTickColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
    ): SliderTrackColors = SliderTrackColors(
        activeTrackColor = activeTrackColor,
        activeTickColor = activeTickColor,
        inactiveTrackColor = inactiveTrackColor,
        inactiveTickColor = inactiveTickColor,
        disabledActiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(.4f),
        disabledActiveTickColor = MaterialTheme.colorScheme.onBackground.copy(.4f),
        disabledInactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(.4f),
        disabledInactiveTickColor = MaterialTheme.colorScheme.onBackground.copy(.4f),
        activeTickTextColor = MaterialTheme.colorScheme.onBackground.copy(.8f),
    )

}
