package com.fingerspell.tutor.presentation.gameover

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.ContentOverlay
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.Ochre
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.core.ui.PaperCard
import com.fingerspell.tutor.core.ui.PrimaryButton
import com.fingerspell.tutor.core.ui.SecondaryButton
import com.fingerspell.tutor.core.ui.SectionHeader
import com.fingerspell.tutor.core.ui.StatCard
import com.fingerspell.tutor.domain.model.DrillResult
import com.fingerspell.tutor.domain.model.NeutralHandshape

@Composable
fun GameOverRoute(
    result: DrillResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onOpenLetter: (String) -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(result) {
        gameOverViewModel.load()
    }
    GameOverScreen(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu,
        onOpenLetter = onOpenLetter
    )
}

@Composable
fun GameOverScreen(
    result: DrillResult,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onOpenLetter: (String) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val sweep = remember { Animatable(0f) }
    LaunchedEffect(result) {
        if (state.haptics) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        sweep.snapTo(0f)
        sweep.animateTo(
            targetValue = result.accuracy / 100f,
            animationSpec = tween(GameConfig.SCORE_RING_MS, easing = FastOutSlowInEasing)
        )
    }
    val tone = skinTone(state.skinToneIndex)
    val statsVisible = listOf(result.correct, result.bestStreak, result.newLetters).count { it > 0 } >= 2

    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.drill_complete),
                    color = InkPrimary,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(150.dp)) {
                    val stroke = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    val inset = stroke.width / 2f
                    val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
                    drawArc(
                        color = Hairline,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke
                    )
                    drawArc(
                        color = Teal,
                        startAngle = 135f,
                        sweepAngle = 270f * sweep.value,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.value_percent, result.accuracy),
                        color = Teal,
                        fontSize = 38.sp,
                        lineHeight = 44.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = stringResource(R.string.stat_accuracy),
                        color = InkSecondary,
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                        letterSpacing = 2.sp
                    )
                }
            }
            if (statsVisible) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        value = result.correct.toString(),
                        label = stringResource(R.string.stat_correct),
                        accent = Teal,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = result.bestStreak.toString(),
                        label = stringResource(R.string.stat_best_streak),
                        accent = Coral,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = result.newLetters.toString(),
                        label = stringResource(R.string.stat_new_letters),
                        accent = Ochre,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            PaperCard(innerPadding = 16) {
                SectionHeader(text = stringResource(R.string.confused_pairs))
                Spacer(modifier = Modifier.height(10.dp))
                if (result.confusedPairs.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_confusions),
                        color = Teal,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                } else {
                    result.confusedPairs.forEach { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenLetter(pair.target) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HandshapeCanvas(
                                spec = state.specs[pair.target] ?: NeutralHandshape,
                                size = 44.dp,
                                tone = tone,
                                description = stringResource(R.string.cd_handshape, pair.target),
                                mirrored = state.mirrored
                            )
                            HandshapeCanvas(
                                spec = state.specs[pair.picked] ?: NeutralHandshape,
                                size = 44.dp,
                                tone = tone,
                                description = stringResource(R.string.cd_handshape, pair.picked),
                                mirrored = state.mirrored
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(
                                    R.string.confused_row,
                                    pair.target,
                                    pair.picked,
                                    pair.count
                                ),
                                color = InkPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.review_note, result.scheduledTomorrow),
                color = InkSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
