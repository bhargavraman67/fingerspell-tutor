package com.fingerspell.tutor.domain.model

data class Lesson(
    val id: Int,
    val letters: List<String>,
    val unit: Int
)
