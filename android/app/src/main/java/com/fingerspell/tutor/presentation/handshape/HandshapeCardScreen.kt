package com.fingerspell.tutor.presentation.handshape

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.fingerspell.tutor.core.theme.BlushVariant
import com.fingerspell.tutor.core.theme.Coral
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.InkSecondary
import com.fingerspell.tutor.core.theme.PlainOverlay
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.PaperCard
import com.fingerspell.tutor.core.ui.PrimaryButton
import com.fingerspell.tutor.core.ui.RotatableHandshape
import com.fingerspell.tutor.core.ui.SecondaryButton
import com.fingerspell.tutor.core.ui.SectionHeader
import com.fingerspell.tutor.core.ui.TopBar

@Composable
fun HandshapeCardRoute(
    letter: String,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onMirror: (String) -> Unit,
    onDrill: (List<String>) -> Unit
) {
    val handshapeViewModel: HandshapeCardViewModel = viewModel(factory = ServiceLocator.factory)
    val state by handshapeViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(letter) {
        handshapeViewModel.load(letter)
    }
    HandshapeCardScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onStep = handshapeViewModel::step,
        onMirror = { onMirror(state.letter) },
        onDrill = { onDrill(state.drillLetters) }
    )
}

@Composable
fun HandshapeCardScreen(
    state: HandshapeCardUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onStep: (Int) -> Unit,
    onMirror: () -> Unit,
    onDrill: () -> Unit
) {
    AppBackdrop(overlay = PlainOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.handshape_title),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = state.letter,
                    color = Coral,
                    fontSize = 34.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(end = 18.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                RotatableHandshape(
                    spec = state.spec,
                    size = 300.dp,
                    tone = skinTone(state.skinToneIndex),
                    description = stringResource(R.string.cd_handshape_rotatable, state.letter),
                    mirrored = state.mirrored
                )
            }
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                PaperCard(innerPadding = 16) {
                    SectionHeader(text = stringResource(R.string.how_to_form))
                    Spacer(modifier = Modifier.height(10.dp))
                    state.tips.forEach { tip ->
                        Text(
                            text = stringResource(tip),
                            color = InkPrimary,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
                if (state.confusables.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(
                            R.string.looks_like,
                            state.confusables.joinToString(" ")
                        ),
                        color = InkSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BlushVariant, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SecondaryButton(
                        text = stringResource(R.string.mirror),
                        onClick = onMirror,
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        PrimaryButton(
                            text = stringResource(R.string.drill),
                            onClick = onDrill,
                            height = 48
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onStep(-1) }) {
                        Text(
                            text = stringResource(R.string.prev_letter),
                            color = InkSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp
                        )
                    }
                    TextButton(onClick = { onStep(1) }) {
                        Text(
                            text = stringResource(R.string.next_letter),
                            color = Coral,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
