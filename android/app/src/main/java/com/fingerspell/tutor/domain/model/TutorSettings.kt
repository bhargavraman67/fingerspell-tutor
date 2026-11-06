package com.fingerspell.tutor.domain.model

enum class Handedness {
    LEFT,
    RIGHT
}

data class TutorSettings(
    val handedness: Handedness = Handedness.RIGHT,
    val speedIndex: Int = 2,
    val skinToneIndex: Int = 1,
    val haptics: Boolean = true
)
