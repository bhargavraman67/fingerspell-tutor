package com.fingerspell.tutor.presentation.menu

import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape

data class MenuUiState(
    val ready: Boolean = false,
    val knownCount: Int = 0,
    val accuracy: Int = 0,
    val bestStreak: Int = 0,
    val dueCount: Int = 0,
    val lessonNumber: Int = 1,
    val lessonLetters: List<String> = emptyList(),
    val heroLetter: String = "A",
    val heroSpec: HandshapeSpec = NeutralHandshape,
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false
)
