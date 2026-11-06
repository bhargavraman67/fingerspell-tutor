package com.fingerspell.tutor.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.Teal

@Composable
fun SegmentedStrip(
    segments: List<Color>,
    modifier: Modifier = Modifier,
    thickness: Int = 4
) {
    Row(
        modifier = modifier.height(thickness.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        segments.forEach { segment ->
            val tint by animateColorAsState(targetValue = segment, label = "segment")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(tint, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun ProgressStrip(
    fraction: Float,
    modifier: Modifier = Modifier,
    thickness: Int = 6,
    fill: Color = Teal
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness.dp)
            .background(Hairline, RoundedCornerShape(3.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(fill, RoundedCornerShape(3.dp))
        )
    }
}
