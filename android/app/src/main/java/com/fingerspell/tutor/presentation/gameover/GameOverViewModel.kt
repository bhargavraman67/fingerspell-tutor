package com.fingerspell.tutor.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val content: ContentRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val prefs = settings.load()
            _state.update {
                it.copy(
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT,
                    haptics = prefs.haptics,
                    specs = content.handshapes().associate { shape -> shape.letter to shape.spec }
                )
            }
        }
    }
}
