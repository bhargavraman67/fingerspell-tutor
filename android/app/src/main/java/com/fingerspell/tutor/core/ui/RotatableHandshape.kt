package com.fingerspell.tutor.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.HandshapeSpec
import kotlinx.coroutines.launch

@Composable
fun RotatableHandshape(
    spec: HandshapeSpec,
    size: Dp,
    tone: Color,
    modifier: Modifier = Modifier,
    description: String? = null,
    mirrored: Boolean = false
) {
    val rotation = remember { Animatable(0f).apply { updateBounds(-75f, 75f) } }
    val scope = rememberCoroutineScope()
    val dragState = rememberDraggableState { delta ->
        scope.launch { rotation.snapTo((rotation.value + delta * 0.35f).coerceIn(-75f, 75f)) }
    }
    Box(
        modifier = modifier
            .size(size)
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    rotation.animateDecay(velocity * 0.08f, exponentialDecay(frictionMultiplier = 0.85f))
                    rotation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(GameConfig.ROTATION_SETTLE_MS, easing = FastOutSlowInEasing)
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        HandshapeCanvas(
            spec = spec,
            size = size,
            tone = tone,
            description = description,
            mirrored = mirrored,
            rotationDeg = rotation.value
        )
    }
}
