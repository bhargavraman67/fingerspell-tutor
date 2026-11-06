package com.fingerspell.tutor.presentation.mirror

import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape

data class PracticeMirrorUiState(
    val letter: String = "A",
    val spec: HandshapeSpec = NeutralHandshape,
    val tips: List<Int> = emptyList(),
    val checked: List<Boolean> = emptyList(),
    val countdown: Int = 0,
    val holding: Boolean = false,
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false,
    val haptics: Boolean = true
)
