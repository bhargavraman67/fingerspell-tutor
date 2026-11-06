package com.fingerspell.tutor.presentation.mirror

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import com.fingerspell.tutor.domain.usecase.PromoteLetterUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PracticeMirrorViewModel(
    private val content: ContentRepository,
    private val settings: SettingsRepository,
    private val progress: ProgressRepository,
    private val promoteLetter: PromoteLetterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PracticeMirrorUiState())
    val state: StateFlow<PracticeMirrorUiState> = _state.asStateFlow()

    private var holdJob: Job? = null

    fun load(letter: String) {
        viewModelScope.launch {
            val prefs = settings.load()
            val shape = content.handshape(letter)
            _state.update {
                it.copy(
                    letter = shape.letter,
                    spec = shape.spec,
                    tips = shape.tips,
                    checked = List(shape.tips.size) { false },
                    countdown = 0,
                    holding = false,
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT,
                    haptics = prefs.haptics
                )
            }
        }
    }

    fun toggleCheck(index: Int) {
        _state.update { current ->
            val next = current.checked.toMutableList()
            if (index in next.indices) next[index] = !next[index]
            current.copy(checked = next)
        }
    }

    fun startHold() {
        if (_state.value.holding) return
        holdJob?.cancel()
        holdJob = viewModelScope.launch {
            _state.update { it.copy(holding = true, countdown = GameConfig.HOLD_SECONDS) }
            while (_state.value.countdown > 0) {
                delay(1000L)
                _state.update { it.copy(countdown = it.countdown - 1) }
            }
            _state.update { it.copy(holding = false) }
        }
    }

    fun rate(succeeded: Boolean) {
        viewModelScope.launch {
            val learner = progress.snapshot()
            progress.save(promoteLetter(learner, _state.value.letter, succeeded, progress.today()))
        }
    }

    fun stop() {
        holdJob?.cancel()
    }
}
