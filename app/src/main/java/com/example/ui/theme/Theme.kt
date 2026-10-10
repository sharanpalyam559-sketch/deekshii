package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
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
    outline = PastelSurfaceOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPastelLavenderPrimary,
    onPrimary = DarkPastelBackground,
    primaryContainer = DarkPastelLavenderContainer,
    onPrimaryContainer = DarkPastelLavenderOnContainer,
    secondary = DarkPastelBlushPink,
    onSecondary = DarkPastelBackground,
    secondaryContainer = DarkPastelPinkContainer,
    onSecondaryContainer = DarkPastelPinkOnContainer,
    tertiary = DarkPastelSageMint,
    onTertiary = DarkPastelBackground,
    tertiaryContainer = DarkPastelMintContainer,
    onTertiaryContainer = DarkPastelMintOnContainer,
    background = DarkPastelBackground,
    onBackground = DarkPastelTextPrimary,
    surface = DarkPastelSurface,
    onSurface = DarkPastelTextPrimary,
    surfaceVariant = DarkPastelSurfaceVariant,
    onSurfaceVariant = DarkPastelTextSecondary,
    outline = DarkPastelSurfaceOutline
)

val RoundedShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

object DeekshiiTheme {
    val colors: DeekshiiPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalDeekshiiPalette.current
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val palette = if (darkTheme) DarkDeekshiiPalette else LightDeekshiiPalette

    CompositionLocalProvider(LocalDeekshiiPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = RoundedShapes,
            content = content
        )
    }
}
