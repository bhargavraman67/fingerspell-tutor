package com.fingerspell.tutor.domain.usecase

import com.fingerspell.tutor.domain.model.LearnerProgress

class GetDueReviewsUseCase {

    operator fun invoke(progress: LearnerProgress, today: Int): List<String> =
        progress.dueDays.filter { it.value <= today }.keys.sorted()
}
