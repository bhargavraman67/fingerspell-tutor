package com.fingerspell.tutor.domain.model

sealed interface DrillMode {
    data class LessonSet(val letters: List<String>) : DrillMode
    data object Review : DrillMode
    data object Mixed : DrillMode
}
