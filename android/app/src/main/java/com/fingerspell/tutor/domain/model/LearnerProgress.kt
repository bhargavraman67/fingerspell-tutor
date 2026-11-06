package com.fingerspell.tutor.domain.model

data class LearnerProgress(
    val boxes: Map<String, Int> = emptyMap(),
    val dueDays: Map<String, Int> = emptyMap(),
    val bestStreak: Int = 0,
    val lifetimeCorrect: Int = 0,
    val lifetimeTotal: Int = 0,
    val lessonsDone: Set<Int> = emptySet(),
    val confusions: Map<String, Int> = emptyMap()
) {
    fun knownCount(knownBox: Int): Int = boxes.count { it.value >= knownBox }

    val accuracy: Int get() = if (lifetimeTotal == 0) 0 else lifetimeCorrect * 100 / lifetimeTotal
}
