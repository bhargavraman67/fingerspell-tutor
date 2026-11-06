package com.fingerspell.tutor.domain.usecase

import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.DrillAnswer
import com.fingerspell.tutor.domain.model.LearnerProgress

class UpdateReviewScheduleUseCase {

    operator fun invoke(
        progress: LearnerProgress,
        answers: List<DrillAnswer>,
        today: Int
    ): LearnerProgress {
        val boxes = progress.boxes.toMutableMap()
        val due = progress.dueDays.toMutableMap()
        val confusions = progress.confusions.toMutableMap()
        var streak = 0
        var best = progress.bestStreak
        answers.forEach { answer ->
            val current = boxes[answer.target] ?: 1
            val next = if (answer.correct) {
                streak++
                if (streak > best) best = streak
                (current + 1).coerceAtMost(GameConfig.MAX_BOX)
            } else {
                streak = 0
                val key = answer.target + answer.picked
                confusions[key] = (confusions[key] ?: 0) + 1
                1
            }
            boxes[answer.target] = next
            due[answer.target] = today + GameConfig.LEITNER_INTERVALS_DAYS[next - 1]
        }
        return progress.copy(
            boxes = boxes,
            dueDays = due,
            confusions = confusions,
            bestStreak = best,
            lifetimeCorrect = progress.lifetimeCorrect + answers.count { it.correct },
            lifetimeTotal = progress.lifetimeTotal + answers.size
        )
    }

    fun scheduledFor(progress: LearnerProgress, day: Int): Int =
        progress.dueDays.count { it.value == day }
}
