package com.fingerspell.tutor.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
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
import com.fingerspell.tutor.core.theme.BlushVariant
import com.fingerspell.tutor.core.theme.ContentOverlay
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.MutedPlum
import com.fingerspell.tutor.core.theme.Ochre
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AnswerTile
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.core.ui.SegmentedStrip
import com.fingerspell.tutor.core.ui.TileState
import com.fingerspell.tutor.domain.model.DrillMode
import com.fingerspell.tutor.domain.model.DrillResult
import com.fingerspell.tutor.presentation.dialog.QuitDrillDialog

@Composable
fun GameRoute(
    mode: DrillMode,
    onFinished: (DrillResult) -> Unit,
    onQuit: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    var askQuit by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(mode) {
        gameViewModel.startDrill(mode)
    }
    LaunchedEffect(Unit) {
        gameViewModel.results.collect { onFinished(it) }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.stop() }
    }
    GameScreen(
        state = state,
        onAnswer = gameViewModel::answer,
        onPause = { askQuit = true }
    )
    if (askQuit) {
        QuitDrillDialog(
            onConfirm = {
                askQuit = false
                onQuit()
            },
            onDismiss = { askQuit = false }
        )
    }
}

@Composable
fun GameScreen(
    state: GameUiState,
    onAnswer: (String) -> Unit,
    onPause: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val bloom = remember { Animatable(0f) }
    LaunchedEffect(state.feedbackTick) {
        if (state.feedbackTick > 0) {
            if (state.haptics) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            bloom.snapTo(0f)
            bloom.animateTo(1f, tween(GameConfig.FEEDBACK_BLOOM_MS))
        }
    }
    val selectedLabel = stringResource(R.string.state_selected)
    val plainLabel = stringResource(R.string.state_not_selected)

    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable(onClick = onPause)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = stringResource(
                        R.string.game_question_counter,
                        state.index + 1,
                        state.total
                    ),
                    color = InkSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    modifier = Modifier.weight(1f)
                )
                SegmentedStrip(
                    segments = state.segments.map {
                        when (it) {
                            SegmentState.CORRECT -> Teal
                            SegmentState.WRONG -> Coral
                            SegmentState.PENDING -> Hairline
                        }
                    },
                    modifier = Modifier.width(110.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .semantics { liveRegion = LiveRegionMode.Polite },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.game_prompt),
                    color = InkSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 1.4.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(contentAlignment = Alignment.Center) {
                    if (state.phase == GamePhase.ANSWERED && state.lastCorrect) {
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .scale(0.6f + bloom.value * 0.65f)
                                .alpha(0.35f * (1f - bloom.value))
                                .background(Teal, RoundedCornerShape(120.dp))
                        )
                    }
                    HandshapeCanvas(
                        spec = state.spec,
                        size = 268.dp,
                        tone = skinTone(state.skinToneIndex),
                        description = stringResource(R.string.cd_handshape, state.target),
                        mirrored = state.mirrored,
                        highlight = if (state.phase == GamePhase.ANSWERED && state.lastCorrect) Teal else null
                    )
                }
                if (state.phase == GamePhase.ANSWERED) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (state.lastCorrect) {
                            stringResource(R.string.game_mark_correct)
                        } else {
                            stringResource(R.string.game_mark_wrong)
                        },
                        color = if (state.lastCorrect) Teal else MutedPlum,
                        fontSize = 40.sp,
                        lineHeight = 46.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(BlushVariant)
                    .padding(horizontal = 20.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InlineStat(
                    label = stringResource(R.string.stat_correct),
                    value = state.correctCount.toString(),
                    accent = Teal
                )
                InlineStat(
                    label = stringResource(R.string.stat_streak),
                    value = state.streak.toString(),
                    accent = Ochre
                )
                InlineStat(
                    label = stringResource(R.string.stat_accuracy),
                    value = stringResource(R.string.value_percent, state.accuracy),
                    accent = Coral
                )
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = PaperWhite,
                shadowElevation = 3.dp
            ) {
                BoxWithConstraints(modifier = Modifier.padding(16.dp)) {
                    val gap = GameConfig.ANSWER_TILE_GAP_DP.dp
                    val tileWidth = (maxWidth - gap) / 2
                    val tileHeight = GameConfig.ANSWER_TILE_HEIGHT_DP.dp
                    Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                        state.options.chunked(2).forEach { pair ->
                            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                                pair.forEach { option ->
                                    val tileState = when {
                                        state.phase != GamePhase.ANSWERED -> TileState.IDLE
                                        option == state.target -> TileState.CORRECT
                                        option == state.picked -> TileState.WRONG
                                        else -> TileState.MUTED
                                    }
                                    AnswerTile(
                                        label = option,
                                        state = tileState,
                                        enabled = state.phase == GamePhase.IDLE,
                                        description = stringResource(
                                            R.string.state_answer_option,
                                            option,
                                            if (option == state.picked) selectedLabel else plainLabel
                                        ),
                                        onClick = { onAnswer(option) },
                                        modifier = Modifier
                                            .width(tileWidth)
                                            .height(tileHeight)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InlineStat(label: String, value: String, accent: androidx.compose.ui.graphics.Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            color = InkSecondary,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 1.2.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = value,
            color = accent,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
