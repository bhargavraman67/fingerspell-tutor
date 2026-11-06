package com.fingerspell.tutor.presentation.game

import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape

enum class GamePhase {
    LOADING,
    IDLE,
    ANSWERED,
    FINISHED
}

enum class SegmentState {
    PENDING,
    CORRECT,
    WRONG
}

data class GameUiState(
    val phase: GamePhase = GamePhase.LOADING,
    val index: Int = 0,
    val total: Int = GameConfig.QUESTIONS_PER_DRILL,
    val target: String = "",
    val options: List<String> = emptyList(),
    val picked: String = "",
    val spec: HandshapeSpec = NeutralHandshape,
    val segments: List<SegmentState> = emptyList(),
    val correctCount: Int = 0,
    val streak: Int = 0,
    val answered: Int = 0,
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false,
    val haptics: Boolean = true,
    val feedbackTick: Int = 0,
    val lastCorrect: Boolean = false
) {
    val accuracy: Int get() = if (answered == 0) 0 else correctCount * 100 / answered
}
