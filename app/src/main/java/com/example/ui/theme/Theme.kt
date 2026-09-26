package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LuxuryDarkColorScheme =
  darkColorScheme(
    primary = GoldPrimary,
    onPrimary = DeepBlack,
    primaryContainer = GoldMuted,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = DeepBlack,
    secondaryContainer = CardBackground,
    onSecondaryContainer = TextGoldSecondary,
    tertiary = EmeraldGreen,
    onTertiary = Color.White,
    background = DeepBlack,
    onBackground = TextWhite,
    surface = SurfaceDark,
    onSurface = TextWhite,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextGoldSecondary,
    outline = CardBorderGold,
    error = RubyRed,
    onError = Color.White
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep false by default so users always experience the Black and Gold luxury theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = LuxuryDarkColorScheme

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = DeepBlack.toArgb()
      window.navigationBarColor = SurfaceDark.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
