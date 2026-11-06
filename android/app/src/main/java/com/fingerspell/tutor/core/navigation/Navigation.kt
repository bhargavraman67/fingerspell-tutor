package com.fingerspell.tutor.core.navigation

import com.fingerspell.tutor.domain.model.DrillMode
import com.fingerspell.tutor.domain.model.DrillResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LessonPath : Screen
    data class HandshapeCard(val letter: String) : Screen
    data class Game(val mode: DrillMode) : Screen
    data class GameOver(val result: DrillResult) : Screen
    data object WordReader : Screen
    data class PracticeMirror(val letter: String) : Screen
    data object EverydaySigns : Screen
    data object Glossary : Screen
    data object CultureNotes : Screen
    data object Settings : Screen
}
