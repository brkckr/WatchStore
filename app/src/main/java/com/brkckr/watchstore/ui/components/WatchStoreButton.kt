package com.brkckr.watchstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brkckr.watchstore.ui.theme.WatchStoreTheme

// primary brand button used throughout the store
@Composable
fun WatchStoreButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    enabled: Boolean = true,
    cornerRadius: Dp = WatchStoreTheme.radius.button,
    shape: Shape? = null,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    val finalShape = shape ?: RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(finalShape)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        WatchStoreTheme.colors.cobalt,
                        WatchStoreTheme.colors.cobaltDark,
                        WatchStoreTheme.colors.cobalt,
                    ),
                ),
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (content != null) {
            content()
        } else {
            Text(
                text = text,
                style = WatchStoreTheme.typography.action,
                color = WatchStoreTheme.colors.paper,
            )
        }
    }
}
