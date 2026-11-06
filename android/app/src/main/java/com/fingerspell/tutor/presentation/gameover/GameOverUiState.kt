package com.fingerspell.tutor.presentation.gameover

import com.fingerspell.tutor.domain.model.HandshapeSpec

data class GameOverUiState(
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false,
    val haptics: Boolean = true,
    val specs: Map<String, HandshapeSpec> = emptyMap()
)
