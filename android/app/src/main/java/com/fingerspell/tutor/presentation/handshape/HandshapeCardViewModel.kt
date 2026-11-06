package com.fingerspell.tutor.presentation.handshape

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

class HandshapeCardViewModel(
    private val content: ContentRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HandshapeCardUiState())
    val state: StateFlow<HandshapeCardUiState> = _state.asStateFlow()

    fun load(letter: String) {
        viewModelScope.launch {
            val prefs = settings.load()
            apply(letter, prefs.skinToneIndex, prefs.handedness == Handedness.LEFT)
        }
    }

    fun step(delta: Int) {
        val letters = content.handshapes().map { it.letter }
        val index = letters.indexOf(_state.value.letter)
        if (index < 0) return
        val next = letters[(index + delta + letters.size) % letters.size]
        apply(next, _state.value.skinToneIndex, _state.value.mirrored)
    }

    private fun apply(letter: String, toneIndex: Int, mirrored: Boolean) {
        val shape = content.handshape(letter)
        val lesson = content.lessons().firstOrNull { it.letters.contains(letter) }
        _state.update {
            it.copy(
                letter = shape.letter,
                spec = shape.spec,
                tips = shape.tips,
                confusables = shape.confusables,
                drillLetters = lesson?.letters ?: listOf(letter),
                skinToneIndex = toneIndex,
                mirrored = mirrored
            )
        }
    }
}
