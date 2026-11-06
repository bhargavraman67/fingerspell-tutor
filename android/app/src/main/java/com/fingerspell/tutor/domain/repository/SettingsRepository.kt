package com.fingerspell.tutor.domain.repository

import com.fingerspell.tutor.domain.model.TutorSettings

interface SettingsRepository {
    fun load(): TutorSettings
    fun save(settings: TutorSettings)
}
