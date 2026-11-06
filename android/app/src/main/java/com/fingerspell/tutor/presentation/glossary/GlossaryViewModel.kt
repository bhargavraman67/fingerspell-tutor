package com.fingerspell.tutor.presentation.glossary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.ProgressRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlossaryViewModel(
    private val content: ContentRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GlossaryUiState())
    val state: StateFlow<GlossaryUiState> = _state.asStateFlow()

    private var allRows: List<GlossaryRow> = emptyList()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val learner = progress.snapshot()
            val prefs = settings.load()
            val letters = content.handshapes().map { shape ->
                val box = learner.boxes[shape.letter] ?: 0
                GlossaryRow(
                    id = "L" + shape.letter,
                    kind = EntryKind.LETTER,
                    letter = shape.letter,
                    spec = shape.spec,
                    knowledge = when {
                        box >= GameConfig.KNOWN_BOX -> KnowledgeState.KNOWN
                        box > 0 -> KnowledgeState.LEARNING
                        else -> KnowledgeState.NEW
                    }
                )
            }
            val signs = content.signs().map { sign ->
                GlossaryRow(
                    id = "S" + sign.id,
                    kind = EntryKind.SIGN,
                    labelRes = sign.nameRes,
                    searchKey = sign.searchKey,
                    spec = sign.spec,
                    motion = sign.motion,
                    knowledge = KnowledgeState.NEW
                )
            }
            allRows = letters + signs
            _state.update {
                it.copy(
                    rows = filtered(it.query, it.filterIndex),
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT
                )
            }
        }
    }

    fun setQuery(value: String) {
        _state.update { it.copy(query = value, rows = filtered(value, it.filterIndex)) }
    }

    fun setFilter(index: Int) {
        _state.update { it.copy(filterIndex = index, rows = filtered(it.query, index)) }
    }

    private fun filtered(query: String, filterIndex: Int): List<GlossaryRow> {
        val needle = query.trim().uppercase()
        return allRows.filter { row ->
            val matchesFilter = when (filterIndex) {
                1 -> row.kind == EntryKind.LETTER
                2 -> row.kind == EntryKind.SIGN
                3 -> row.knowledge == KnowledgeState.KNOWN
                else -> true
            }
            val label = if (row.kind == EntryKind.LETTER) row.letter else row.searchKey
            matchesFilter && (needle.isEmpty() || label.uppercase().contains(needle))
        }
    }
}
