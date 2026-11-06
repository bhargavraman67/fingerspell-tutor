package com.fingerspell.tutor.core.theme

import androidx.compose.ui.graphics.Color

object AppPreset {
    const val name = "WARM_EARTHY"
}

val Blush = Color(0xFFFFF4F0)
val PaperWhite = Color(0xFFFFFFFF)
val BlushVariant = Color(0xFFFDEBE5)
val Hairline = Color(0xFFF7D6CC)
val Coral = Color(0xFFD1495B)
val CoralDark = Color(0xFFB03A4B)
val CoralLight = Color(0xFFE0656F)
val Teal = Color(0xFF00798C)
val TealLight = Color(0xFF35A3B3)
val Ochre = Color(0xFFC77C3E)
val MutedPlum = Color(0xFF8E5A60)
val InkPrimary = Color(0xFF2B1B1E)
val InkSecondary = Color(0xFF6B5055)
val OnCoral = Color(0xFFFFF4F0)

val SplashOverlay = listOf(Color(0xE62B1B1E), Color(0xF2451F2B), Color(0xE8D1495B))
val MenuOverlay = listOf(Color(0xF2FFF4F0), Color(0xFAFFF4F0), Color(0xF5F7D6CC))
val ContentOverlay = listOf(Color(0xF7FFF4F0), Color(0xFAFFF4F0), Color(0xF7FDEBE5))
val PlainOverlay = listOf(Color(0xFAFFF4F0), Color(0xFCFFF4F0), Color(0xFAFFF4F0))

val SkinTones = listOf(
    Color(0xFFF2D3C2),
    Color(0xFFE0B093),
    Color(0xFFB97E5B),
    Color(0xFF7A4B32)
)

fun skinTone(index: Int): Color = SkinTones[index.coerceIn(0, SkinTones.lastIndex)]
