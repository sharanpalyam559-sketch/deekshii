package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = PastelLavenderPrimary,
    onPrimary = PastelSurface,
    primaryContainer = PastelLavenderContainer,
    onPrimaryContainer = PastelLavenderOnContainer,
    secondary = PastelBlushPink,
    onSecondary = PastelSurface,
    secondaryContainer = PastelPinkContainer,
    onSecondaryContainer = PastelPinkOnContainer,
    tertiary = PastelSageMint,
    onTertiary = PastelSurface,
    tertiaryContainer = PastelMintContainer,
    onTertiaryContainer = PastelMintOnContainer,
    background = PastelBackground,
    onBackground = PastelTextPrimary,
    surface = PastelSurface,
    onSurface = PastelTextPrimary,
    surfaceVariant = PastelSurfaceVariant,
    onSurfaceVariant = PastelTextSecondary,
    outline = PastelSurfaceOutline,
)

private val DarkColorScheme = darkColorScheme(
    primary = PastelLavenderPrimary,
    onPrimary = PastelSurface,
    primaryContainer = PastelLavenderDark,
    onPrimaryContainer = PastelLavenderContainer,
    secondary = PastelBlushPink,
    onSecondary = PastelSurface,
    secondaryContainer = PastelPinkOnContainer,
    onSecondaryContainer = PastelPinkContainer,
    tertiary = PastelSageMint,
    onTertiary = PastelSurface,
    background = PastelTextPrimary,
    onBackground = PastelBackground,
    surface = Color(0xFF352D42),
    onSurface = PastelBackground,
    surfaceVariant = Color(0xFF453C54),
    onSurfaceVariant = PastelLavenderLight,
    outline = Color(0xFF5E5470)
)

val RoundedShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep pastel theme intact
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RoundedShapes,
        content = content
    )
}
