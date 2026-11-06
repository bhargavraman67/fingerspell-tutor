package com.fingerspell.tutor.domain.model

data class WordGroup(
    val id: Int,
    val labelRes: Int
)

data class WordEntry(
    val word: String,
    val groupId: Int
)
