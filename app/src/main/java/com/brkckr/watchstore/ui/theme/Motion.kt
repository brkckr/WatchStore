package com.brkckr.watchstore.ui.theme

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class WatchStoreMotion(
    val durationLong: Int = 1000,
    val durationMedium: Int = 600,
    val durationShort: Int = 400,
    val standardEasing: Easing = FastOutSlowInEasing
)

val LocalWatchStoreMotion = staticCompositionLocalOf { WatchStoreMotion() }
