package com.fingerspell.tutor.domain.model

data class DrillQuestion(
    val target: String,
    val options: List<String>
)

data class DrillAnswer(
    val target: String,
    val picked: String
) {
    val correct: Boolean get() = target == picked
}
