package com.fingerspell.tutor.presentation.lessonpath

import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape

data class LessonRow(
    val id: Int,
    val unit: Int,
    val letters: List<String> = emptyList(),
    val topicLabelRes: Int = 0,
    val complete: Boolean = false,
    val current: Boolean = false,
    val spec: HandshapeSpec = NeutralHandshape
)

data class LessonPathUiState(
    val rows: List<LessonRow> = emptyList(),
    val knownCount: Int = 0,
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false
)
