package com.fingerspell.tutor.data.repository

import com.fingerspell.tutor.data.sample.SampleData
import com.fingerspell.tutor.domain.model.Handshape
import com.fingerspell.tutor.domain.model.Lesson
import com.fingerspell.tutor.domain.model.SignEntry
import com.fingerspell.tutor.domain.model.SignTopic
import com.fingerspell.tutor.domain.model.WordEntry
import com.fingerspell.tutor.domain.model.WordGroup
import com.fingerspell.tutor.domain.repository.ContentRepository

class ContentRepositoryImpl : ContentRepository {

    override fun handshapes(): List<Handshape> = SampleData.handshapes

    override fun handshape(letter: String): Handshape =
        SampleData.handshapes.firstOrNull { it.letter == letter } ?: SampleData.handshapes.first()

    override fun lessons(): List<Lesson> = SampleData.lessons

    override fun topics(): List<SignTopic> = SampleData.topics

    override fun signs(): List<SignEntry> = SampleData.signs

    override fun wordGroups(): List<WordGroup> = SampleData.wordGroups

    override fun words(): List<WordEntry> = SampleData.words
}
