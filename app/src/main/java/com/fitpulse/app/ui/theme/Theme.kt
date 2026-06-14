package com.fitpulse.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Teal500,
    onPrimary = LightSurface,
    primaryContainer = Teal200,
    secondary = Orange500,
    onSecondary = LightSurface,
    background = LightBackground,
    surface = LightSurface
)

private val DarkColors = darkColorScheme(
    primary = Teal200,
    onPrimary = DarkBackground,
    primaryContainer = Teal700,
    secondary = Orange500,
    onSecondary = DarkBackground,
    background = DarkBackground,
    surface = DarkSurface
)

@Composable
fun FitPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
