package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// --- Sleek Interface Theme Colors (Dark Slate & Ice Blue Palette) ---

// Dark Palette
val SleekBackgroundDark = Color(0xFF111318)       // Deepest charcoal slate
val SleekSurfaceDark = Color(0xFF1A1C1E)          // Sleek card and sheet surface
val SleekSurfaceVariantDark = Color(0xFF2A2D31)   // Selected button/highlight container background
val SleekPrimaryDark = Color(0xFFD1E4FF)          // Soft futuristic ice blue for active buttons and major accents
val SleekOnPrimaryDark = Color(0xFF00315C)        // Deep contrast blue for text inside ice blue buttons
val SleekSecondaryDark = Color(0xFFA8C7FF)        // Sleek cyan-blue highlight for headings and subtitles
val SleekOnSecondaryDark = Color(0xFF002246)
val SleekTertiaryDark = Color(0xFF38495F)         // Medium slate tint for toggles, indicators, and minor frames
val SleekTextMainDark = Color(0xFFE2E2E6)         // Crisp white-gray for main reading body text
val SleekAccentCoral = Color(0xFFFFB4AB)          // Sharp coral accent for maps, drops, and alerts

// Light Palette (Elegantly adapted from Sleek Interface)
val SleekBackgroundLight = Color(0xFFF4F6FA)      // Clean cool gray-blue background
val SleekSurfaceLight = Color(0xFFFFFFFF)         // Pure white cards
val SleekSurfaceVariantLight = Color(0xFFE1E2E5)  // Light gray-blue accent container
val SleekPrimaryLight = Color(0xFF004786)         // Deep sapphire blue
val SleekOnPrimaryLight = Color(0xFFFFFFFF)       // Crisp white text on primary
val SleekSecondaryLight = Color(0xFF38495F)       // Slate gray for sub-headers
val SleekOnSecondaryLight = Color(0xFFFFFFFF)
val SleekTertiaryLight = Color(0xFFD1E4FF)        // Soft ice blue for highlight accents
val SleekTextMainLight = Color(0xFF111318)        // Charcoal black for reading body text

// Deprecated colors mapped safely to prevent unresolved references in legacy files
val SaffronPrimary = SleekPrimaryLight
val SaffronLight = SleekPrimaryDark
val SaffronDark = SleekSecondaryLight
val GoldAccent = SleekSecondaryDark
val GoldDark = SleekSecondaryLight
val SlateDark = SleekTertiaryDark
val SlateMedium = SleekTertiaryDark
val DarkBackground = SleekBackgroundDark
val DarkSurface = SleekSurfaceDark
val DarkSurfaceVariant = SleekSurfaceVariantDark
val LightBackground = SleekBackgroundLight
val LightSurface = SleekSurfaceLight
val LightSurfaceVariant = SleekSurfaceVariantLight
val LightBorder = SleekSurfaceVariantLight

