package com.fingerspell.tutor.domain.usecase

import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.ConfusedPair
import com.fingerspell.tutor.domain.model.DrillAnswer
import com.fingerspell.tutor.domain.model.DrillResult
import com.fingerspell.tutor.domain.model.LearnerProgress

class ScoreDrillUseCase {

    operator fun invoke(
        answers: List<DrillAnswer>,
        before: LearnerProgress,
        after: LearnerProgress,
        scheduledTomorrow: Int
    ): DrillResult {
        var streak = 0
        var best = 0
        answers.forEach {
            if (it.correct) {
                streak++
                if (streak > best) best = streak
            } else {
                streak = 0
            }
        }
        val threshold = GameConfig.KNOWN_BOX
        val promoted = after.boxes.count { entry ->
            entry.value >= threshold && (before.boxes[entry.key] ?: 0) < threshold
        }
        val pairs = answers.filter { !it.correct }
            .groupBy { Pair(it.target, it.picked) }
            .map { ConfusedPair(it.key.first, it.key.second, it.value.size) }
            .sortedByDescending { it.count }
            .take(3)
        return DrillResult(
            total = answers.size,
            correct = answers.count { it.correct },
            bestStreak = best,
            newLetters = promoted,
            confusedPairs = pairs,
            scheduledTomorrow = scheduledTomorrow
        )
    }
}
