package com.fingerspell.tutor.presentation.wordreader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.repository.ContentRepository
import com.fingerspell.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class WordReaderViewModel(
    private val content: ContentRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WordReaderUiState())
    val state: StateFlow<WordReaderUiState> = _state.asStateFlow()

    private var spellJob: Job? = null

    fun start() {
        viewModelScope.launch {
            val prefs = settings.load()
            _state.update {
                it.copy(
                    groupLabels = content.wordGroups().map { group -> group.labelRes },
                    speedIndex = prefs.speedIndex.coerceIn(0, GameConfig.LETTER_SPEED_STEPS.lastIndex),
                    speedMs = GameConfig.LETTER_SPEED_STEPS[
                        prefs.speedIndex.coerceIn(0, GameConfig.LETTER_SPEED_STEPS.lastIndex)
                    ],
                    skinToneIndex = prefs.skinToneIndex,
                    mirrored = prefs.handedness == Handedness.LEFT
                )
            }
            nextWord()
        }
    }

    fun selectGroup(index: Int) {
        _state.update { it.copy(groupIndex = index) }
        nextWord()
    }

    fun nextWord() {
        val groupId = content.wordGroups()[_state.value.groupIndex.coerceIn(0, 2)].id
        val pool = content.words().filter { it.groupId == groupId }
        val target = pool[Random.nextInt(pool.size)].word
        val others = pool.map { it.word }.filter { it != target }.shuffled().take(2)
        _state.update {
            it.copy(
                word = target,
                choices = (others + target).shuffled(),
                picked = "",
                revealed = 0,
                phase = ReaderPhase.SPELLING
            )
        }
        spell()
    }

    fun spell() {
        spellJob?.cancel()
        spellJob = viewModelScope.launch {
            val word = _state.value.word
            _state.update { it.copy(phase = ReaderPhase.SPELLING, revealed = 0) }
            word.forEachIndexed { index, char ->
                val letter = char.toString()
                _state.update {
                    it.copy(
                        revealed = index + 1,
                        currentLetter = letter,
                        spec = content.handshape(letter).spec
                    )
                }
                delay(_state.value.speedMs.toLong())
            }
            _state.update { it.copy(phase = ReaderPhase.ANSWERING) }
        }
    }

    fun pick(choice: String) {
        if (_state.value.phase != ReaderPhase.ANSWERING) return
        _state.update { it.copy(picked = choice, phase = ReaderPhase.REVEALED) }
    }

    fun setSpeed(index: Int) {
        val safe = index.coerceIn(0, GameConfig.LETTER_SPEED_STEPS.lastIndex)
        viewModelScope.launch {
            val prefs = settings.load()
            settings.save(prefs.copy(speedIndex = safe))
            _state.update {
                it.copy(speedIndex = safe, speedMs = GameConfig.LETTER_SPEED_STEPS[safe])
            }
        }
    }

    fun stop() {
        spellJob?.cancel()
    }
}
