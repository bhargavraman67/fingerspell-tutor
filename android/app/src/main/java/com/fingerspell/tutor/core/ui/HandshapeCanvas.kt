package com.fingerspell.tutor.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.fingerspell.tutor.domain.model.FingerState
import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.ThumbPose

private const val UNIT_W = 100f
private const val UNIT_H = 130f
private val OutlineInk = Color(0xE62B1B1E)
private val InnerShade = Color(0x142B1B1E)

@Composable
fun HandshapeCanvas(
    spec: HandshapeSpec,
    size: Dp,
    tone: Color,
    modifier: Modifier = Modifier,
    description: String? = null,
    mirrored: Boolean = false,
    rotationDeg: Float = 0f,
    highlight: Color? = null
) {
    val semanticsModifier = if (description == null) {
        modifier
    } else {
        modifier.semantics { contentDescription = description }
    }
    Canvas(modifier = semanticsModifier.size(size)) {
        val unit = this.size.height / UNIT_H
        val insetX = (this.size.width - UNIT_W * unit) / 2f
        val pivot = Offset(this.size.width / 2f, this.size.height / 2f)
        withTransform({
            if (mirrored) scale(-1f, 1f, pivot)
            rotate(spec.palmRotationDeg + rotationDeg, pivot)
            translate(insetX, 0f)
            scale(unit, unit, Offset.Zero)
        }) {
            drawHand(spec, tone, highlight)
        }
    }
}

private fun DrawScope.drawHand(spec: HandshapeSpec, tone: Color, highlight: Color?) {
    val stroke = Stroke(width = 2.0f)
    val palmLeft = 23f
    val palmTop = 66f
    val palmW = 54f
    val palmH = 60f

    drawThumb(spec.thumb, tone, stroke)

    val roots = listOf(32f, 44f, 56f, 68f)
    val lengths = listOf(42f, 48f, 44f, 34f)
    val states = listOf(spec.index, spec.middle, spec.ring, spec.little)
    for (i in 0 until 4) {
        drawFinger(
            rootX = roots[i],
            rootY = palmTop + 6f,
            baseLen = lengths[i],
            state = states[i],
            fan = (i - 1.5f) * spec.spread * 11f,
            tone = tone,
            stroke = stroke
        )
    }

    drawRoundRect(
        color = tone,
        topLeft = Offset(palmLeft, palmTop),
        size = Size(palmW, palmH),
        cornerRadius = CornerRadius(18f, 18f)
    )
    drawRoundRect(
        color = InnerShade,
        topLeft = Offset(palmLeft + 4f, palmTop + 7f),
        size = Size(palmW - 8f, palmH - 11f),
        cornerRadius = CornerRadius(14f, 14f)
    )
    drawRoundRect(
        color = OutlineInk,
        topLeft = Offset(palmLeft, palmTop),
        size = Size(palmW, palmH),
        cornerRadius = CornerRadius(18f, 18f),
        style = stroke
    )
    if (highlight != null) {
        drawRoundRect(
            color = highlight,
            topLeft = Offset(palmLeft - 4f, palmTop - 4f),
            size = Size(palmW + 8f, palmH + 8f),
            cornerRadius = CornerRadius(22f, 22f),
            style = Stroke(width = 3.0f)
        )
    }
}

private fun DrawScope.drawFinger(
    rootX: Float,
    rootY: Float,
    baseLen: Float,
    state: FingerState,
    fan: Float,
    tone: Color,
    stroke: Stroke
) {
    if (state == FingerState.TUCKED) return
    val width = 14f
    val total = baseLen * state.factor
    val firstLen = if (state == FingerState.EXTENDED) total else total * 0.62f
    rotate(fan, Offset(rootX, rootY)) {
        capsule(rootX, rootY, width, firstLen + 8f, tone, stroke)
        if (state != FingerState.EXTENDED) {
            val jointY = rootY - firstLen
            val bend = if (state == FingerState.BENT) 68f else 118f
            rotate(bend, Offset(rootX, jointY)) {
                capsule(rootX, jointY, width * 0.92f, total * 0.6f + 6f, tone, stroke)
            }
            drawCircle(color = tone, radius = width * 0.52f, center = Offset(rootX, jointY))
            drawCircle(
                color = OutlineInk,
                radius = width * 0.52f,
                center = Offset(rootX, jointY),
                style = stroke
            )
        }
    }
}

private fun DrawScope.drawThumb(pose: ThumbPose, tone: Color, stroke: Stroke) {
    val config = when (pose) {
        ThumbPose.OUT -> floatArrayOf(26f, 110f, -54f, 38f)
        ThumbPose.ACROSS -> floatArrayOf(75f, 100f, 96f, 46f)
        ThumbPose.UP -> floatArrayOf(25f, 108f, -10f, 40f)
        ThumbPose.TUCKED -> floatArrayOf(38f, 106f, -18f, 20f)
        ThumbPose.SIDE -> floatArrayOf(25f, 112f, -24f, 32f)
    }
    rotate(config[2], Offset(config[0], config[1])) {
        capsule(config[0], config[1], 17f, config[3], tone, stroke)
    }
}

private fun DrawScope.capsule(
    centerX: Float,
    baseY: Float,
    width: Float,
    length: Float,
    tone: Color,
    stroke: Stroke
) {
    val topLeft = Offset(centerX - width / 2f, baseY - length)
    val size = Size(width, length)
    val radius = CornerRadius(width / 2f, width / 2f)
    drawRoundRect(color = tone, topLeft = topLeft, size = size, cornerRadius = radius)
    drawRoundRect(color = OutlineInk, topLeft = topLeft, size = size, cornerRadius = radius, style = stroke)
}
