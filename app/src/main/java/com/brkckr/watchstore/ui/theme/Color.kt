package com.brkckr.watchstore.ui.theme

import androidx.compose.ui.graphics.Color

// custom color palette for the watch store theme
val Ink = Color(0xFF121725)
val MutedInk = Color(0xFF717889)
val Porcelain = Color(0xFFF1F2F4)
val Paper = Color(0xFFFFFFFF)
val Cobalt = Color(0xFF22387F)
val CobaltDark = Color(0xFF09269E)

data class WatchStoreColors(
    val ink: Color = Ink,
    val mutedInk: Color = MutedInk,
    val porcelain: Color = Porcelain,
    val paper: Color = Paper,
    val cobalt: Color = Cobalt,
    val cobaltDark: Color = CobaltDark
)

val LocalWatchStoreColors = androidx.compose.runtime.staticCompositionLocalOf { WatchStoreColors() }
