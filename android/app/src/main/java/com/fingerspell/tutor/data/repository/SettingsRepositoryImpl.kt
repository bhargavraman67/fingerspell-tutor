package com.fingerspell.tutor.data.repository

import com.fingerspell.tutor.data.local.TutorPreferences
import com.fingerspell.tutor.domain.model.Handedness
import com.fingerspell.tutor.domain.model.TutorSettings
import com.fingerspell.tutor.domain.repository.SettingsRepository

private const val KEY_HANDEDNESS = "handedness"
private const val KEY_SPEED = "speed_index"
private const val KEY_TONE = "skin_tone"
private const val KEY_HAPTICS = "haptics"

class SettingsRepositoryImpl(private val prefs: TutorPreferences) : SettingsRepository {

    override fun load(): TutorSettings = TutorSettings(
        handedness = if (prefs.int(KEY_HANDEDNESS, 1) == 0) Handedness.LEFT else Handedness.RIGHT,
        speedIndex = prefs.int(KEY_SPEED, 2),
        skinToneIndex = prefs.int(KEY_TONE, 1),
        haptics = prefs.bool(KEY_HAPTICS, true)
    )

    override fun save(settings: TutorSettings) {
        prefs.putInt(KEY_HANDEDNESS, if (settings.handedness == Handedness.LEFT) 0 else 1)
        prefs.putInt(KEY_SPEED, settings.speedIndex)
        prefs.putInt(KEY_TONE, settings.skinToneIndex)
        prefs.putBool(KEY_HAPTICS, settings.haptics)
    }
}
