package com.fingerspell.tutor.domain.model

enum class FingerState(val factor: Float) {
    EXTENDED(1.0f),
    BENT(0.62f),
    CURLED(0.30f),
    TUCKED(0.0f)
}

enum class ThumbPose {
    OUT,
    ACROSS,
    UP,
    TUCKED,
    SIDE
}

data class HandshapeSpec(
    val index: FingerState,
    val middle: FingerState,
    val ring: FingerState,
    val little: FingerState,
    val thumb: ThumbPose,
    val palmRotationDeg: Float = 0f,
    val spread: Float = 0f
)

val NeutralHandshape = HandshapeSpec(
    index = FingerState.EXTENDED,
    middle = FingerState.EXTENDED,
    ring = FingerState.EXTENDED,
    little = FingerState.EXTENDED,
    thumb = ThumbPose.OUT,
    palmRotationDeg = 0f,
    spread = 0.8f
)
