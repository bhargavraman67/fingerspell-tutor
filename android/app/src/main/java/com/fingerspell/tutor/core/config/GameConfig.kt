package com.fingerspell.tutor.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val SCREEN_TRANSITION_MS = 240
    const val FEEDBACK_BLOOM_MS = 180
    const val FEEDBACK_HOLD_MS = 900L
    const val ROTATION_SETTLE_MS = 200
    const val SIGN_LOOP_MS = 1400
    const val SCORE_RING_MS = 700
    const val QUESTIONS_PER_DRILL = 10
    const val OPTIONS_PER_QUESTION = 4
    const val ANSWER_TILE_HEIGHT_DP = 64
    const val ANSWER_TILE_GAP_DP = 12
    const val KNOWN_BOX = 3
    const val MAX_BOX = 5
    const val HOLD_SECONDS = 5
    const val TOTAL_LETTERS = 26
    val LETTER_SPEED_STEPS = listOf(1200, 900, 700, 500, 350)
    val LEITNER_INTERVALS_DAYS = listOf(1, 2, 4, 7, 14)
}
