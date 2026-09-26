package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep Luxury Black & Gold Palette
val DeepBlack = Color(0xFF0E0E12)
val SurfaceDark = Color(0xFF16171D)
val CardBackground = Color(0xFF1E2028)
val CardBorderGold = Color(0xFF383324)

// Gold Accents & Studio Spotlight Brush
val GoldPrimary = Color(0xFFE5C158)
val GoldLight = Color(0xFFF7DF88)
val GoldDark = Color(0xFFB89635)
val GoldMuted = Color(0xFF7D6C3B)
val GoldAmber = Color(0xFFFFD700)

val GoldSpotlightBrush = Brush.radialGradient(
  colors = listOf(
    Color(0xFFF3E279), // Bright golden center spotlight
    Color(0xFFDFBD43), // Warm golden mid-tone
    Color(0xFFA68022), // Metallic amber gold
    Color(0xFF5E420C), // Dark bronze ring
    Color(0xFF2C1C03), // Vignette amber shadow
    Color(0xFF140C01)  // Dark outer edge
  )
)

// Text & Neutral Colors
val TextWhite = Color(0xFFF7F7F9)
val TextGoldSecondary = Color(0xFFC9C4B5)
val TextMuted = Color(0xFF8E8D98)

// Status Colors
val EmeraldGreen = Color(0xFF2EBD70)
val EmeraldLight = Color(0xFF1E3A2D)
val RubyRed = Color(0xFFE55B5B)
val RubyLight = Color(0xFF3E2325)
val CyanBlue = Color(0xFF38BDF8)
