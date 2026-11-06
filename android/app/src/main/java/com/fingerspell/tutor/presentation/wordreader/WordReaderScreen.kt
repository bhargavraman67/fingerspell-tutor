package com.fingerspell.tutor.presentation.wordreader

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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.ContentOverlay
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AnswerTile
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.ChipRow
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.core.ui.PrimaryButton
import com.fingerspell.tutor.core.ui.SecondaryButton
import com.fingerspell.tutor.core.ui.TileState
import com.fingerspell.tutor.core.ui.TopBar

@Composable
fun WordReaderRoute(onBack: () -> Unit, onHome: () -> Unit) {
    val wordReaderViewModel: WordReaderViewModel = viewModel(factory = ServiceLocator.factory)
    val state by wordReaderViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        wordReaderViewModel.start()
    }
    DisposableEffect(Unit) {
        onDispose { wordReaderViewModel.stop() }
    }
    WordReaderScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onGroup = wordReaderViewModel::selectGroup,
        onPick = wordReaderViewModel::pick,
        onReplay = wordReaderViewModel::spell,
        onNext = wordReaderViewModel::nextWord,
        onSpeed = wordReaderViewModel::setSpeed
    )
}

@Composable
fun WordReaderScreen(
    state: WordReaderUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onGroup: (Int) -> Unit,
    onPick: (String) -> Unit,
    onReplay: () -> Unit,
    onNext: () -> Unit,
    onSpeed: (Int) -> Unit
) {
    val selectedLabel = stringResource(R.string.state_selected)
    val plainLabel = stringResource(R.string.state_not_selected)
    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.word_reader_title),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            ChipRow(
                items = state.groupLabels.map { stringResource(it) },
                selectedIndex = state.groupIndex,
                onSelect = onGroup,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                HandshapeCanvas(
                    spec = state.spec,
                    size = 236.dp,
                    tone = skinTone(state.skinToneIndex),
                    description = stringResource(R.string.cd_handshape, state.currentLetter),
                    mirrored = state.mirrored
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                state.word.forEachIndexed { index, char ->
                    val shown = index < state.revealed || state.phase == ReaderPhase.REVEALED
                    Text(
                        text = if (shown) char.toString() else "_",
                        color = when {
                            index == state.revealed - 1 && state.phase == ReaderPhase.SPELLING -> Coral
                            shown -> InkSecondary
                            else -> Hairline
                        },
                        fontSize = 26.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.word_prompt),
                color = InkSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.choices.forEach { choice ->
                    val tileState = when {
                        state.phase != ReaderPhase.REVEALED -> TileState.IDLE
                        choice == state.word -> TileState.CORRECT
                        choice == state.picked -> TileState.WRONG
                        else -> TileState.MUTED
                    }
                    AnswerTile(
                        label = choice,
                        state = tileState,
                        enabled = state.phase == ReaderPhase.ANSWERING,
                        description = stringResource(
                            R.string.state_answer_option,
                            choice,
                            if (choice == state.picked) selectedLabel else plainLabel
                        ),
                        onClick = { onPick(choice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(
                        R.string.word_speed,
                        state.speedIndex + 1,
                        state.speedMs
                    ),
                    color = InkSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    letterSpacing = 1.2.sp
                )
                Slider(
                    value = state.speedIndex.toFloat(),
                    onValueChange = { onSpeed(it.toInt()) },
                    valueRange = 0f..4f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = Coral,
                        activeTrackColor = Teal,
                        inactiveTrackColor = Hairline
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    PrimaryButton(
                        text = stringResource(R.string.spell_again),
                        onClick = onReplay,
                        height = 52
                    )
                }
                SecondaryButton(
                    text = stringResource(R.string.next_word),
                    onClick = onNext,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
