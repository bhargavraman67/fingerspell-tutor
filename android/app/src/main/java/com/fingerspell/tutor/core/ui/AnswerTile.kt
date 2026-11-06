package com.fingerspell.tutor.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fingerspell.tutor.core.theme.BlushVariant
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.MutedPlum
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.Teal

enum class TileState {
    IDLE,
    CORRECT,
    WRONG,
    MUTED
}

@Composable
fun AnswerTile(
    label: String,
    state: TileState,
    enabled: Boolean,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val press by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessHigh),
        label = "tilePress"
    )
    val shake = remember { Animatable(0f) }
    LaunchedEffect(state) {
        if (state == TileState.WRONG) {
            repeat(3) {
                shake.animateTo(8f, tween(44))
                shake.animateTo(-8f, tween(44))
            }
            shake.animateTo(0f, tween(44))
        } else {
            shake.snapTo(0f)
        }
    }
    val border = when (state) {
        TileState.CORRECT -> Teal
        TileState.WRONG -> MutedPlum
        else -> Hairline
    }
    val fill = when (state) {
        TileState.CORRECT -> Teal.copy(alpha = 0.12f)
        TileState.WRONG -> MutedPlum.copy(alpha = 0.10f)
        else -> PaperWhite
    }
    val textColor = when (state) {
        TileState.CORRECT -> Teal
        TileState.WRONG -> MutedPlum
        else -> InkPrimary
    }
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .scale(press)
            .offset { IntOffset(shake.value.toInt(), 0) }
            .alpha(if (state == TileState.MUTED) 0.45f else 1f)
            .background(if (pressed) BlushVariant else fill, RoundedCornerShape(14.dp))
            .border(BorderStroke(1.5.dp, border), RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, interactionSource = interactionSource, indication = null, onClick = onClick)
            .semantics { stateDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 28.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
