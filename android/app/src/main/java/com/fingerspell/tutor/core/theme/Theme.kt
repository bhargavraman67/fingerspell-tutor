package com.fingerspell.tutor.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TutorColorScheme = lightColorScheme(
    primary = Coral,
    onPrimary = OnCoral,
    primaryContainer = CoralLight,
    onPrimaryContainer = OnCoral,
    secondary = Teal,
    onSecondary = OnCoral,
    secondaryContainer = BlushVariant,
    onSecondaryContainer = InkPrimary,
    tertiary = Ochre,
    onTertiary = OnCoral,
    background = Blush,
    onBackground = InkPrimary,
    surface = PaperWhite,
    onSurface = InkPrimary,
    surfaceVariant = BlushVariant,
    onSurfaceVariant = InkSecondary,
    outline = Hairline,
    outlineVariant = Hairline,
    error = MutedPlum,
    onError = OnCoral
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TutorColorScheme,
        typography = AppTypography,
        content = content
    )
}
