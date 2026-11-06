package com.fingerspell.tutor.presentation.mirror

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.BlushVariant
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.PlainOverlay
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.core.ui.PaperCard
import com.fingerspell.tutor.core.ui.PrimaryButton
import com.fingerspell.tutor.core.ui.SecondaryButton
import com.fingerspell.tutor.core.ui.SectionHeader
import com.fingerspell.tutor.core.ui.TopBar

@Composable
fun PracticeMirrorRoute(
    letter: String,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val mirrorViewModel: PracticeMirrorViewModel = viewModel(factory = ServiceLocator.factory)
    val state by mirrorViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(letter) {
        mirrorViewModel.load(letter)
    }
    DisposableEffect(Unit) {
        onDispose { mirrorViewModel.stop() }
    }
    PracticeMirrorScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onHold = mirrorViewModel::startHold,
        onToggle = mirrorViewModel::toggleCheck,
        onRate = { succeeded ->
            mirrorViewModel.rate(succeeded)
            onBack()
        }
    )
}

@Composable
fun PracticeMirrorScreen(
    state: PracticeMirrorUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onHold: () -> Unit,
    onToggle: (Int) -> Unit,
    onRate: (Boolean) -> Unit
) {
    val tone = skinTone(state.skinToneIndex)
    val checkedLabel = stringResource(R.string.state_checked)
    val uncheckedLabel = stringResource(R.string.state_unchecked)
    AppBackdrop(overlay = PlainOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.mirror_title),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HandshapeCanvas(
                        spec = state.spec,
                        size = 170.dp,
                        tone = tone,
                        description = stringResource(R.string.cd_handshape, state.letter),
                        mirrored = state.mirrored
                    )
                    Text(
                        text = stringResource(R.string.mirror_target),
                        color = InkSecondary,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        letterSpacing = 2.sp
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Hairline)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HandshapeCanvas(
                        spec = state.spec,
                        size = 170.dp,
                        tone = tone,
                        description = stringResource(R.string.cd_handshape_mirrored, state.letter),
                        mirrored = !state.mirrored
                    )
                    Text(
                        text = stringResource(R.string.mirror_as_you_see),
                        color = InkSecondary,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(
                text = stringResource(R.string.mirror_privacy),
                color = InkSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlushVariant)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                PrimaryButton(
                    text = if (state.holding) {
                        stringResource(R.string.mirror_countdown, state.countdown)
                    } else {
                        stringResource(R.string.hold_five)
                    },
                    onClick = onHold,
                    enabled = !state.holding,
                    height = 56
                )
                Spacer(modifier = Modifier.height(14.dp))
                PaperCard(innerPadding = 16) {
                    SectionHeader(text = stringResource(R.string.mirror_checklist))
                    Spacer(modifier = Modifier.height(10.dp))
                    state.tips.forEachIndexed { index, tip ->
                        val isChecked = state.checked.getOrElse(index) { false }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggle(index) }
                                .padding(vertical = 7.dp)
                                .semantics {
                                    stateDescription = if (isChecked) checkedLabel else uncheckedLabel
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(
                                        if (isChecked) Teal else Hairline,
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    Text(
                                        text = stringResource(R.string.game_mark_correct),
                                        color = androidx.compose.ui.graphics.Color.White,
                                        fontSize = 13.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(tip),
                                color = InkPrimary,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.not_yet),
                        onClick = { onRate(false) },
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        PrimaryButton(
                            text = stringResource(R.string.got_it),
                            onClick = { onRate(true) },
                            height = 48
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}
