package com.fingerspell.tutor.domain.repository

import com.fingerspell.tutor.domain.model.LearnerProgress

interface ProgressRepository {
    fun snapshot(): LearnerProgress
    fun save(progress: LearnerProgress)
    fun today(): Int
    fun clear()
}
