package com.apptolast.fiscsitmonitor.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val FicsitColorScheme = darkColorScheme(
    primary = AccentGreen,
    onPrimary = TextOnAccent,
    primaryContainer = AccentGreen20,
    onPrimaryContainer = AccentGreen,
    secondary = AccentCyan,
    onSecondary = TextOnAccent,
    tertiary = AccentPurple,
    onTertiary = TextOnAccent,
    background = BgPrimary,
    onBackground = TextPrimary,
    surface = BgSurface,
    onSurface = TextPrimary,
    surfaceVariant = BgCard,
    onSurfaceVariant = TextSecondary,
    surfaceContainerHigh = BgElevated,
    outline = Border,
    outlineVariant = BorderLight,
    error = AccentRed,
    onError = TextPrimary,
    errorContainer = AccentRed20,
    onErrorContainer = AccentRed,
)

data class FicsitColors(
    val accentGreen: Color = AccentGreen,
    val accentGreen20: Color = AccentGreen20,
    val accentRed: Color = AccentRed,
    val accentRed20: Color = AccentRed20,
    val accentYellow: Color = AccentYellow,
    val accentOrange: Color = AccentOrange,
    val accentOrange20: Color = AccentOrange20,
    val accentCyan: Color = AccentCyan,
    val accentPurple: Color = AccentPurple,
    val bgCard: Color = BgCard,
    val bgElevated: Color = BgElevated,
    val textMuted: Color = TextMuted,
    val textSecondary: Color = TextSecondary,
    val border: Color = Border,
    val borderLight: Color = BorderLight,
)

val LocalFicsitColors = staticCompositionLocalOf { FicsitColors() }

@Composable
fun FicsitMonitorTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFicsitColors provides FicsitColors()) {
        MaterialTheme(
            colorScheme = FicsitColorScheme,
            typography = FicsitTypography,
            shapes = FicsitShapes,
            content = content,
        )
    }
}

object FicsitTheme {
    val colors: FicsitColors
        @Composable get() = LocalFicsitColors.current
}
