package com.fingerspell.tutor.domain.model

data class Handshape(
    val letter: String,
    val spec: HandshapeSpec,
    val tips: List<Int>,
    val confusables: List<String>
)
