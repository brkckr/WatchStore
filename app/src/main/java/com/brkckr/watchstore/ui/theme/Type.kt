package com.brkckr.watchstore.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// typography styles for the watch store
val Typography = Typography(
    displaySmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.6).sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 27.sp,
        lineHeight = 31.sp,
        letterSpacing = (-0.3).sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 27.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 25.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 0.8.sp,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        letterSpacing = 2.2.sp,
    ),
)

data class WatchStoreTypography(
    val price: TextStyle = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
    val action: TextStyle = Typography.labelLarge.copy(fontSize = 14.sp, letterSpacing = 1.2.sp),
    val specLabel: TextStyle = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.2.sp),
    val specValue: TextStyle = Typography.labelLarge.copy(fontSize = 14.sp)
)

val LocalWatchStoreTypography = androidx.compose.runtime.staticCompositionLocalOf { WatchStoreTypography() }
