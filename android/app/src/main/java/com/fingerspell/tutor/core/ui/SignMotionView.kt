package com.fingerspell.tutor.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.SignMotion
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SignMotionView(
    spec: HandshapeSpec,
    motion: SignMotion,
    size: Dp,
    tone: Color,
    modifier: Modifier = Modifier,
    description: String? = null,
    mirrored: Boolean = false
) {
    val transition = rememberInfiniteTransition(label = "signLoop")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(GameConfig.SIGN_LOOP_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "signPhase"
    )
    val turn = (phase * 2f * PI).toFloat()
    val values = when (motion) {
        SignMotion.TAP_CHIN -> Triple(0f, -6f + sin(turn) * 7f, 0f)
        SignMotion.CIRCLE -> Triple(cos(turn) * 10f, sin(turn) * 10f, 0f)
        SignMotion.ARC_OUT -> Triple(
            phase * 20f - 8f,
            -sin(phase * PI.toFloat()) * 14f,
            phase * 10f
        )
        SignMotion.WAVE -> Triple(0f, 0f, sin(turn) * 16f)
        SignMotion.UP_DOWN -> Triple(0f, sin(turn) * 13f, 0f)
        SignMotion.FORWARD -> Triple(sin(turn) * 6f, -phase * 12f + 5f, sin(turn) * 5f)
    }
    val shiftX = values.first
    val shiftY = values.second
    val tilt = values.third
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        HandshapeCanvas(
            spec = spec,
            size = size * 0.82f,
            tone = tone,
            modifier = Modifier.offset(x = shiftX.dp, y = shiftY.dp),
            description = description,
            mirrored = mirrored,
            rotationDeg = tilt
        )
    }
}
