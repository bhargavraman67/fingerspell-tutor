package com.fingerspell.tutor.presentation.signs

import com.fingerspell.tutor.domain.model.SignEntry

data class EverydaySignsUiState(
    val topicIndex: Int = 0,
    val topicLabels: List<Int> = emptyList(),
    val signs: List<SignEntry> = emptyList(),
    val selected: SignEntry? = null,
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false
)
