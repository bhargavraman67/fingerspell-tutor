package com.fingerspell.tutor.domain.repository

import com.fingerspell.tutor.domain.model.Handshape
import com.fingerspell.tutor.domain.model.Lesson
import com.fingerspell.tutor.domain.model.SignEntry
import com.fingerspell.tutor.domain.model.SignTopic
import com.fingerspell.tutor.domain.model.WordEntry
import com.fingerspell.tutor.domain.model.WordGroup

interface ContentRepository {
    fun handshapes(): List<Handshape>
    fun handshape(letter: String): Handshape
    fun lessons(): List<Lesson>
    fun topics(): List<SignTopic>
    fun signs(): List<SignEntry>
    fun wordGroups(): List<WordGroup>
    fun words(): List<WordEntry>
}
