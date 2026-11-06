package com.fingerspell.tutor.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.fingerspell.tutor.core.theme.PaperWhite
import com.fingerspell.tutor.core.theme.SkinTones
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.PaperCard
import com.fingerspell.tutor.core.ui.SecondaryButton
import com.fingerspell.tutor.core.ui.SectionHeader
import com.fingerspell.tutor.core.ui.TopBar
import com.fingerspell.tutor.core.ui.TutorChip
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.presentation.dialog.ResetProgressDialog

@Composable
fun SettingsRoute(onBack: () -> Unit, onHome: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    var askReset by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        settingsViewModel.refresh()
    }
    SettingsScreen(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onHandedness = settingsViewModel::setHandedness,
        onSpeed = settingsViewModel::setSpeed,
        onSkinTone = settingsViewModel::setSkinTone,
        onHaptics = settingsViewModel::setHaptics,
        onReset = { askReset = true }
    )
    if (askReset) {
        ResetProgressDialog(
            onConfirm = {
                askReset = false
                settingsViewModel.resetProgress()
            },
            onDismiss = { askReset = false }
        )
    }
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onHandedness: (Handedness) -> Unit,
    onSpeed: (Int) -> Unit,
    onSkinTone: (Int) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    val selectedLabel = stringResource(R.string.state_selected)
    val plainLabel = stringResource(R.string.state_not_selected)
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    AppBackdrop(overlay = ContentOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            TopBar(
                title = stringResource(R.string.settings),
                onBack = onBack,
                onHome = onHome,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PaperCard(innerPadding = 16) {
                    SectionHeader(text = stringResource(R.string.settings_handedness))
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        TutorChip(
                            label = stringResource(R.string.settings_left),
                            selected = state.handedness == Handedness.LEFT,
                            onClick = { onHandedness(Handedness.LEFT) }
                        )
                        TutorChip(
                            label = stringResource(R.string.settings_right),
                            selected = state.handedness == Handedness.RIGHT,
                            onClick = { onHandedness(Handedness.RIGHT) }
                        )
                    }
                }
                PaperCard(innerPadding = 16) {
                    SectionHeader(text = stringResource(R.string.settings_speed))
                    Text(
                        text = stringResource(
                            R.string.word_speed,
                            state.speedIndex + 1,
                            state.speedMs
                        ),
                        color = InkSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(top = 8.dp)
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
                PaperCard(innerPadding = 16) {
                    SectionHeader(text = stringResource(R.string.settings_skin))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        SkinTones.forEachIndexed { index, color ->
                            val chosen = index == state.skinToneIndex
                            val label = stringResource(R.string.settings_skin_tone, index + 1)
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(color, CircleShape)
                                    .border(
                                        if (chosen) 3.dp else 1.dp,
                                        if (chosen) Coral else Hairline,
                                        CircleShape
                                    )
                                    .clickable(onClickLabel = label) { onSkinTone(index) }
                                    .semantics {
                                        stateDescription = if (chosen) selectedLabel else plainLabel
                                    }
                            )
                        }
                    }
                }
                PaperCard(innerPadding = 16) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            SectionHeader(text = stringResource(R.string.settings_haptics))
                        }
                        Switch(
                            checked = state.haptics,
                            onCheckedChange = onHaptics,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PaperWhite,
                                checkedTrackColor = Teal,
                                uncheckedThumbColor = PaperWhite,
                                uncheckedTrackColor = Hairline
                            ),
                            modifier = Modifier.semantics {
                                stateDescription = if (state.haptics) onLabel else offLabel
                            }
                        )
                    }
                }
                PaperCard(innerPadding = 16) {
                    SectionHeader(text = stringResource(R.string.settings_progress))
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        text = stringResource(R.string.reset_progress),
                        onClick = onReset,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
