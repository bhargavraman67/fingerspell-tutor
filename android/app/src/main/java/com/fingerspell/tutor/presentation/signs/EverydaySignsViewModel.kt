package com.fingerspell.tutor.presentation.signs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.model.SignEntry
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EverydaySignsViewModel(
    private val content: ContentRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EverydaySignsUiState())
    val state: StateFlow<EverydaySignsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val prefs = settings.load()
            _state.update {
                it.copy(
                    topicLabels = content.topics().map { topic -> topic.labelRes },
                    signs = signsFor(it.topicIndex),
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT
                )
            }
        }
    }

    fun selectTopic(index: Int) {
        _state.update { it.copy(topicIndex = index, signs = signsFor(index)) }
    }

    fun open(sign: SignEntry) {
        _state.update { it.copy(selected = sign) }
    }

    fun close() {
        _state.update { it.copy(selected = null) }
    }

    private fun signsFor(index: Int): List<SignEntry> {
        val topics = content.topics()
        val topic = topics[index.coerceIn(0, topics.lastIndex)]
        return content.signs().filter { it.topicId == topic.id }
    }
}
