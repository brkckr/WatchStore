package com.brkckr.watchstore.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// custom material 3 theme configuration
private val WatchStoreColorScheme = lightColorScheme(
    primary = Cobalt,
    onPrimary = Paper,
    primaryContainer = Porcelain,
    onPrimaryContainer = Ink,
    background = Porcelain,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Porcelain,
    onSurfaceVariant = MutedInk,
    outline = Porcelain,
)

object WatchStoreTheme {
    val colors: WatchStoreColors
        @Composable
        get() = LocalWatchStoreColors.current
    val typography: WatchStoreTypography
        @Composable
        get() = LocalWatchStoreTypography.current
    val radius: WatchStoreRadius
        @Composable
        get() = LocalWatchStoreRadius.current
    val motion: WatchStoreMotion
        @Composable
        get() = LocalWatchStoreMotion.current
}

@Composable
fun WatchStoreTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWatchStoreColors provides WatchStoreColors(),
        LocalWatchStoreTypography provides WatchStoreTypography(),
        LocalWatchStoreRadius provides WatchStoreRadius(),
        LocalWatchStoreMotion provides WatchStoreMotion()
    ) {
        MaterialTheme(
            colorScheme = WatchStoreColorScheme,
            typography = com.brkckr.watchstore.ui.theme.Typography,
            content = content,
        )
    }
}
