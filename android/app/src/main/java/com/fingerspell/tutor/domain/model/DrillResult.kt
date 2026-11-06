package com.fingerspell.tutor.domain.model

data class ConfusedPair(
    val target: String,
    val picked: String,
    val count: Int
)

data class DrillResult(
    val total: Int = 0,
    val correct: Int = 0,
    val bestStreak: Int = 0,
    val newLetters: Int = 0,
    val confusedPairs: List<ConfusedPair> = emptyList(),
    val scheduledTomorrow: Int = 0
) {
    val accuracy: Int get() = if (total == 0) 0 else correct * 100 / total
}
