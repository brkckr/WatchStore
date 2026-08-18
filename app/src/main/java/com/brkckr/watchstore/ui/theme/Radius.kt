package com.brkckr.watchstore.ui.theme

import androidx.compose.ui.unit.dp

// unified corner radius values for consistent design
val RadiusSmall = 8.dp
val RadiusMedium = 12.dp
val RadiusButton = 16.dp
val RadiusLarge = 24.dp
val RadiusExtraLarge = 32.dp

data class WatchStoreRadius(
    val small: androidx.compose.ui.unit.Dp = RadiusSmall,
    val medium: androidx.compose.ui.unit.Dp = RadiusMedium,
    val button: androidx.compose.ui.unit.Dp = RadiusButton,
    val large: androidx.compose.ui.unit.Dp = RadiusLarge,
    val extraLarge: androidx.compose.ui.unit.Dp = RadiusExtraLarge
)

val LocalWatchStoreRadius = androidx.compose.runtime.staticCompositionLocalOf { WatchStoreRadius() }
