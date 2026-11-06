package com.fingerspell.tutor.presentation.glossary

import com.fingerspell.tutor.domain.model.HandshapeSpec
import com.fingerspell.tutor.domain.model.NeutralHandshape
import com.fingerspell.tutor.domain.model.SignMotion

enum class EntryKind {
    LETTER,
    SIGN
}

enum class KnowledgeState {
    NEW,
    LEARNING,
    KNOWN
}

data class GlossaryRow(
    val id: String,
    val kind: EntryKind,
    val letter: String = "",
    val labelRes: Int = 0,
    val searchKey: String = "",
    val spec: HandshapeSpec = NeutralHandshape,
    val motion: SignMotion = SignMotion.WAVE,
    val knowledge: KnowledgeState = KnowledgeState.NEW
)

data class GlossaryUiState(
    val query: String = "",
    val filterIndex: Int = 0,
    val rows: List<GlossaryRow> = emptyList(),
    val skinToneIndex: Int = 1,
    val mirrored: Boolean = false
)
