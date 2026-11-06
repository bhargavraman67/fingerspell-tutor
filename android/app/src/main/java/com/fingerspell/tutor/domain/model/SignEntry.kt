package com.fingerspell.tutor.domain.model

enum class SignMotion {
    TAP_CHIN,
    CIRCLE,
    ARC_OUT,
    WAVE,
    UP_DOWN,
    FORWARD
}

data class SignTopic(
    val id: Int,
    val labelRes: Int
)

data class SignEntry(
    val id: Int,
    val nameRes: Int,
    val descriptionRes: Int,
    val topicId: Int,
    val motion: SignMotion,
    val spec: HandshapeSpec,
    val searchKey: String = ""
)
