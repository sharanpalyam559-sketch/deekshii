package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Light Pastel Palette
val PastelLavenderPrimary = Color(0xFF917BBF)
val PastelLavenderDark = Color(0xFF6750A4)
val PastelLavenderLight = Color(0xFFEBE4F6)
val PastelLavenderContainer = Color(0xFFF3ECFA)
val PastelLavenderOnContainer = Color(0xFF3B2868)

val PastelBlushPink = Color(0xFFF398AE)
val PastelPinkLight = Color(0xFFFCE4EC)
val PastelPinkContainer = Color(0xFFFDF0F4)
val PastelPinkOnContainer = Color(0xFF6B1D34)

val PastelSageMint = Color(0xFF6FBA8C)
val PastelMintLight = Color(0xFFE8F5E9)
val PastelMintContainer = Color(0xFFEBF7EE)
val PastelMintOnContainer = Color(0xFF1B4E2B)

val PastelPeach = Color(0xFFF6A97A)
val PastelPeachLight = Color(0xFFFFF3E0)
val PastelPeachContainer = Color(0xFFFFF8F0)
val PastelPeachOnContainer = Color(0xFF6E360F)

val PastelSkyBlue = Color(0xFF78B5E8)
val PastelSkyLight = Color(0xFFE3F2FD)
val PastelSkyContainer = Color(0xFFEEF6FC)
val PastelSkyOnContainer = Color(0xFF13456F)

val PastelButterYellow = Color(0xFFF5CF63)
val PastelYellowLight = Color(0xFFFFFDE7)

val PastelBackground = Color(0xFFFAF7FD)
val PastelSurface = Color(0xFFFFFFFF)
val PastelSurfaceVariant = Color(0xFFF3EFF8)
val PastelSurfaceOutline = Color(0xFFE2DCE9)
val PastelTextPrimary = Color(0xFF2C2438)
val PastelTextSecondary = Color(0xFF6B6276)
val PastelTextMuted = Color(0xFF9B92A4)

// Dark Midnight Pastel Palette
val DarkPastelBackground = Color(0xFF13101A)
val DarkPastelSurface = Color(0xFF1E1A27)
val DarkPastelSurfaceVariant = Color(0xFF2B2538)
val DarkPastelSurfaceOutline = Color(0xFF3F3650)
val DarkPastelTextPrimary = Color(0xFFF5F1FB)
val DarkPastelTextSecondary = Color(0xFFCBC3D8)
val DarkPastelTextMuted = Color(0xFF988FA6)

val DarkPastelLavenderPrimary = Color(0xFFC7B3EC)
val DarkPastelLavenderDark = Color(0xFF8F76BE)
val DarkPastelLavenderLight = Color(0xFF2E2442)
val DarkPastelLavenderContainer = Color(0xFF35274E)
val DarkPastelLavenderOnContainer = Color(0xFFEFE6FC)

val DarkPastelBlushPink = Color(0xFFF7A2B6)
val DarkPastelPinkLight = Color(0xFF361A25)
val DarkPastelPinkContainer = Color(0xFF421E2C)
val DarkPastelPinkOnContainer = Color(0xFFFDDFE6)

val DarkPastelSageMint = Color(0xFF86CCA0)
val DarkPastelMintLight = Color(0xFF142E1F)
val DarkPastelMintContainer = Color(0xFF183826)
val DarkPastelMintOnContainer = Color(0xFFD6F8E2)

val DarkPastelPeach = Color(0xFFFAB68C)
val DarkPastelPeachLight = Color(0xFF382113)
val DarkPastelPeachContainer = Color(0xFF442B1A)
val DarkPastelPeachOnContainer = Color(0xFFFEE9DB)

val DarkPastelSkyBlue = Color(0xFF90C5F2)
val DarkPastelSkyLight = Color(0xFF132536)
val DarkPastelSkyContainer = Color(0xFF183149)
val DarkPastelSkyOnContainer = Color(0xFFD7EDFD)

@Immutable
data class DeekshiiPalette(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val lavenderPrimary: Color,
    val lavenderContainer: Color,
    val lavenderOnContainer: Color,
    val lavenderLight: Color,
    val pinkPrimary: Color,
    val pinkContainer: Color,
    val pinkOnContainer: Color,
    val pinkLight: Color,
    val mintPrimary: Color,
    val mintContainer: Color,
    val mintOnContainer: Color,
    val peachPrimary: Color,
    val peachContainer: Color,
    val peachOnContainer: Color,
    val skyPrimary: Color,
    val skyContainer: Color,
    val skyOnContainer: Color
)

val LightDeekshiiPalette = DeekshiiPalette(
    isDark = false,
    background = PastelBackground,
    surface = PastelSurface,
    surfaceVariant = PastelSurfaceVariant,
    outline = PastelSurfaceOutline,
    textPrimary = PastelTextPrimary,
    textSecondary = PastelTextSecondary,
    textMuted = PastelTextMuted,
    lavenderPrimary = PastelLavenderPrimary,
    lavenderContainer = PastelLavenderContainer,
    lavenderOnContainer = PastelLavenderOnContainer,
    lavenderLight = PastelLavenderLight,
    pinkPrimary = PastelBlushPink,
    pinkContainer = PastelPinkContainer,
    pinkOnContainer = PastelPinkOnContainer,
    pinkLight = PastelPinkLight,
    mintPrimary = PastelSageMint,
    mintContainer = PastelMintContainer,
    mintOnContainer = PastelMintOnContainer,
    peachPrimary = PastelPeach,
    peachContainer = PastelPeachContainer,
    peachOnContainer = PastelPeachOnContainer,
    skyPrimary = PastelSkyBlue,
    skyContainer = PastelSkyContainer,
    skyOnContainer = PastelSkyOnContainer
)

val DarkDeekshiiPalette = DeekshiiPalette(
    isDark = true,
    background = DarkPastelBackground,
    surface = DarkPastelSurface,
    surfaceVariant = DarkPastelSurfaceVariant,
    outline = DarkPastelSurfaceOutline,
    textPrimary = DarkPastelTextPrimary,
    textSecondary = DarkPastelTextSecondary,
    textMuted = DarkPastelTextMuted,
    lavenderPrimary = DarkPastelLavenderPrimary,
    lavenderContainer = DarkPastelLavenderContainer,
    lavenderOnContainer = DarkPastelLavenderOnContainer,
    lavenderLight = DarkPastelLavenderLight,
    pinkPrimary = DarkPastelBlushPink,
    pinkContainer = DarkPastelPinkContainer,
    pinkOnContainer = DarkPastelPinkOnContainer,
    pinkLight = DarkPastelPinkLight,
    mintPrimary = DarkPastelSageMint,
    mintContainer = DarkPastelMintContainer,
    mintOnContainer = DarkPastelMintOnContainer,
    peachPrimary = DarkPastelPeach,
    peachContainer = DarkPastelPeachContainer,
    peachOnContainer = DarkPastelPeachOnContainer,
    skyPrimary = DarkPastelSkyBlue,
    skyContainer = DarkPastelSkyContainer,
    skyOnContainer = DarkPastelSkyOnContainer
)

val LocalDeekshiiPalette = staticCompositionLocalOf { LightDeekshiiPalette }
