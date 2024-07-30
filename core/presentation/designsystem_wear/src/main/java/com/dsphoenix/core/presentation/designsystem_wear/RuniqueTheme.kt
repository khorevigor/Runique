package com.dsphoenix.core.presentation.designsystem_wear

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Typography
import com.dsphoenix.core.presentation.designsystem.DarkColorScheme
import com.dsphoenix.core.presentation.designsystem.Poppins

private val WearColors = ColorScheme(
    primary = DarkColorScheme.primary,
    primaryContainer = DarkColorScheme.primaryContainer,
    onPrimary = DarkColorScheme.onPrimary,
    onPrimaryContainer = DarkColorScheme.onPrimaryContainer,
    secondary = DarkColorScheme.secondary,
    onSecondary = DarkColorScheme.onSecondary,
    secondaryContainer = DarkColorScheme.onSecondaryContainer,
    onSecondaryContainer = DarkColorScheme.onSecondaryContainer,
    tertiary = DarkColorScheme.tertiary,
    onTertiary = DarkColorScheme.onTertiary,
    tertiaryContainer = DarkColorScheme.tertiaryContainer,
    onTertiaryContainer = DarkColorScheme.onTertiaryContainer,
    surface = DarkColorScheme.surface,
    onSurface = DarkColorScheme.onSurface,
    surfaceDim = DarkColorScheme.surfaceDim,
    onSurfaceVariant = DarkColorScheme.onSurfaceVariant,
    background = DarkColorScheme.background,
    onBackground = DarkColorScheme.onBackground,
    error = DarkColorScheme.error,
    onError = DarkColorScheme.onError
)

private val WearTypography = Typography(
        defaultFontFamily = Poppins
    )

@Composable
fun RuniqueTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WearColors,
        typography = WearTypography
    ) {
        content()
    }
}
