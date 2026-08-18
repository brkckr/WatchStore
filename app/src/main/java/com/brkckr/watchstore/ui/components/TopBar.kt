package com.brkckr.watchstore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.watchstore.R
import com.brkckr.watchstore.ui.theme.WatchStoreTheme

// navigation bar for the watch screens
@Composable
fun WatchTopBar(
    showBack: Boolean,
    cartCount: Int,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onCartPositioned: (Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 18.dp),
    ) {
        AnimatedVisibility(
            visible = showBack,
            enter = fadeIn() + slideInHorizontally { -it },
            exit = fadeOut() + slideOutHorizontally { -it },
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            CircleAction(label = "‹", onClick = onBack)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.labelLarge,
                color = WatchStoreTheme.colors.ink,
                letterSpacing = 2.4.sp,
            )
            Text(
                text = stringResource(R.string.app_subtitle),
                style = WatchStoreTheme.typography.specLabel,
                color = WatchStoreTheme.colors.mutedInk,
                fontSize = 8.sp,
                letterSpacing = 1.8.sp,
            )
        }

        // cart action on the right - visible in both feed and detail
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onGloballyPositioned { onCartPositioned(it.positionInRoot()) },
        ) {
            CircleAction(label = "CART", onClick = onOpenCart)
            if (cartCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .background(WatchStoreTheme.colors.cobalt, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = cartCount.coerceAtMost(99).toString(),
                        color = WatchStoreTheme.colors.paper,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

// navigation bar for the cart screen
@Composable
fun CartTopBar(cartCount: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleAction(label = "‹", onClick = onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                stringResource(R.string.cart_title),
                style = MaterialTheme.typography.headlineMedium,
                color = WatchStoreTheme.colors.ink
            )
            Text(
                text = pluralStringResource(R.plurals.timepiece_count, cartCount, cartCount),
                style = MaterialTheme.typography.bodyMedium,
                color = WatchStoreTheme.colors.mutedInk,
            )
        }
    }
}

// reusable circular button for top bar actions
@Composable
fun CircleAction(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(WatchStoreTheme.colors.paper)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = WatchStoreTheme.colors.ink,
            fontSize = if (label == "‹") 32.sp else 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = if (label == "CART") 0.8.sp else 0.sp,
            modifier = if (label == "‹") Modifier.offset(y = (-2).dp) else Modifier,
        )
    }
}
