package com.fingerspell.tutor.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.model.TutorSettings
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settings: SettingsRepository,
    private val progress: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            publish(settings.load())
        }
    }

    fun setHandedness(value: Handedness) {
        store { it.copy(handedness = value) }
    }

    fun setSpeed(index: Int) {
        store { it.copy(speedIndex = index.coerceIn(0, GameConfig.LETTER_SPEED_STEPS.lastIndex)) }
    }

    fun setSkinTone(index: Int) {
        store { it.copy(skinToneIndex = index) }
    }

    fun setHaptics(enabled: Boolean) {
        store { it.copy(haptics = enabled) }
    }

    fun resetProgress() {
        viewModelScope.launch {
            progress.clear()
        }
    }

    private fun store(transform: (TutorSettings) -> TutorSettings) {
        viewModelScope.launch {
            val updated = transform(settings.load())
            settings.save(updated)
            publish(updated)
        }
    }

    private fun publish(values: TutorSettings) {
        _state.value = SettingsUiState(
            handedness = values.handedness,
            speedIndex = values.speedIndex,
            speedMs = GameConfig.LETTER_SPEED_STEPS[values.speedIndex.coerceIn(0, 4)],
            skinToneIndex = values.skinToneIndex,
            haptics = values.haptics
        )
    }
}
