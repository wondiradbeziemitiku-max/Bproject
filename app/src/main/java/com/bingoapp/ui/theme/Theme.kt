package com.bingoapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = DeepBlue,
    primaryContainer = MediumBlue,
    onPrimaryContainer = LightGold,
    secondary = BingoGreen,
    onSecondary = White,
    secondaryContainer = DarkGreen,
    onSecondaryContainer = White,
    tertiary = BingoRed,
    onTertiary = White,
    tertiaryContainer = DarkRed,
    onTertiaryContainer = White,
    error = BingoRed,
    onError = White,
    background = DeepBlue,
    onBackground = White,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = Gray600,
    outlineVariant = Gray800
)

@Composable
fun BingoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
