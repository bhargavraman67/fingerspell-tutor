package com.fingerspell.tutor.presentation.wordreader

import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape

enum class ReaderPhase {
    SPELLING,
    ANSWERING,
    REVEALED
}

data class WordReaderUiState(
    val phase: ReaderPhase = ReaderPhase.SPELLING,
    val groupIndex: Int = 0,
    val groupLabels: List<Int> = emptyList(),
    val word: String = "",
    val revealed: Int = 0,
    val currentLetter: String = "",
    val spec: HandshapeSpec = NeutralHandshape,
    val choices: List<String> = emptyList(),
    val picked: String = "",
    val speedIndex: Int = 2,
    val speedMs: Int = 700,
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false
)
