package com.fingerspell.tutor.presentation.signs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.ContentOverlay
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.InkPrimary
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.ChipRow
import com.fingerspell.tutor.core.ui.SectionHeader
import com.fingerspell.tutor.core.ui.SignMotionView
import com.fingerspell.tutor.core.ui.TopBar
import com.fingerspell.tutor.presentation.dialog.SignDetailDialog

@Composable
fun EverydaySignsRoute(onBack: () -> Unit, onHome: () -> Unit) {
    val signsViewModel: EverydaySignsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by signsViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        signsViewModel.refresh()
    }
    EverydaySignsScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onTopic = signsViewModel::selectTopic,
        onOpen = signsViewModel::open
    )
    val selected = state.selected
    if (selected != null) {
        SignDetailDialog(
            name = stringResource(selected.nameRes),
            description = stringResource(selected.descriptionRes),
            topic = stringResource(
                state.topicLabels.getOrElse(state.topicIndex) { R.string.signs_title }
            ),
            spec = selected.spec,
            motion = selected.motion,
            tone = skinTone(state.skinToneIndex),
            mirrored = state.mirrored,
            onDismiss = signsViewModel::close
        )
    }
}

@Composable
fun EverydaySignsScreen(
    state: EverydaySignsUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onTopic: (Int) -> Unit,
    onOpen: (com.fingerspell.tutor.domain.model.SignEntry) -> Unit
) {
    val tone = skinTone(state.skinToneIndex)
    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.signs_title),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader(
                    text = stringResource(R.string.unit_signs),
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ChipRow(
                items = state.topicLabels.map { stringResource(it) },
                selectedIndex = state.topicIndex,
                onSelect = onTopic,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.signs) { sign ->
                    Column(
                        modifier = Modifier
                            .height(176.dp)
                            .background(PaperWhite, RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, Hairline), RoundedCornerShape(16.dp))
                            .clickable { onOpen(sign) }
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            SignMotionView(
                                spec = sign.spec,
                                motion = sign.motion,
                                size = 104.dp,
                                tone = tone,
                                description = stringResource(
                                    R.string.cd_sign_motion,
                                    stringResource(sign.nameRes)
                                ),
                                mirrored = state.mirrored
                            )
                        }
                        Text(
                            text = stringResource(sign.nameRes),
                            color = InkPrimary,
                            fontSize = 15.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(
                                state.topicLabels.getOrElse(state.topicIndex) { R.string.signs_title }
                            ),
                            color = Teal,
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            letterSpacing = 1.4.sp
                        )
                    }
                }
            }
        }
    }
}
