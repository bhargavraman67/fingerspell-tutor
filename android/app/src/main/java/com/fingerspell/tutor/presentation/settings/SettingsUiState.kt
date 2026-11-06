package com.fingerspell.tutor.presentation.settings

import com.fingerspell.tutor.domain.model.Handedness

data class SettingsUiState(
    val handedness: Handedness = Handedness.RIGHT,
    val speedIndex: Int = 2,
    val speedMs: Int = 700,
    val skinToneIndex: Int = 1,
    val haptics: Boolean = true
)
