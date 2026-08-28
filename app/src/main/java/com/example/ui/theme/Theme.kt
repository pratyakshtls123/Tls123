package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CosmicColorScheme = darkColorScheme(
    primary = CosmicViolet,
    onPrimary = TextWhite,
    primaryContainer = CosmicIndigoDark,
    onPrimaryContainer = TextWhite,
    secondary = CosmicIndigo,
    onSecondary = TextWhite,
    secondaryContainer = SpaceCardElevated,
    onSecondaryContainer = TextLight,
    tertiary = CosmicCyan,
    onTertiary = SpaceBlack,
    background = SpaceBlack,
    onBackground = TextWhite,
    surface = SpaceCard,
    onSurface = TextWhite,
    surfaceVariant = SpaceCardElevated,
    onSurfaceVariant = TextMuted,
    outline = SpaceBorder,
    outlineVariant = SpaceBorderLight,
    error = CosmicRose,
    onError = TextWhite
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = Typography,
        content = content
    )
}
