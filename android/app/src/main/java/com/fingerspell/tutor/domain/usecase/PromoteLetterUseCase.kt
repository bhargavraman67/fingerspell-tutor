package com.fingerspell.tutor.domain.usecase

import com.fingerspell.tutor.core.config.GameConfig
import com.fingerspell.tutor.domain.model.LearnerProgress

class PromoteLetterUseCase {

    operator fun invoke(
        progress: LearnerProgress,
        letter: String,
        succeeded: Boolean,
        today: Int
    ): LearnerProgress {
        val boxes = progress.boxes.toMutableMap()
        val due = progress.dueDays.toMutableMap()
        val current = boxes[letter] ?: 1
        val next = if (succeeded) (current + 1).coerceAtMost(GameConfig.MAX_BOX) else 1
        boxes[letter] = next
        due[letter] = today + GameConfig.LEITNER_INTERVALS_DAYS[next - 1]
        return progress.copy(boxes = boxes, dueDays = due)
    }
}
