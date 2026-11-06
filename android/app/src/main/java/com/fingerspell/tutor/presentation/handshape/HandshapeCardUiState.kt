package com.fingerspell.tutor.presentation.handshape

import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape

data class HandshapeCardUiState(
    val letter: String = "A",
    val spec: HandshapeSpec = NeutralHandshape,
    val tips: List<Int> = emptyList(),
    val confusables: List<String> = emptyList(),
    val drillLetters: List<String> = emptyList(),
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false
)
