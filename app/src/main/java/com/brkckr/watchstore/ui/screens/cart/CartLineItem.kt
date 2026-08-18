package com.brkckr.watchstore.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.watchstore.R
import com.brkckr.watchstore.model.CartItem
import com.brkckr.watchstore.model.Watch
import com.brkckr.watchstore.ui.components.WatchImage
import com.brkckr.watchstore.ui.theme.WatchStoreTheme
import com.brkckr.watchstore.util.formatPrice

// item row in the cart list with quantity controls
@Composable
fun CartLineItem(
    watch: Watch,
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
) {
    Surface(
        color = WatchStoreTheme.colors.paper,
        shape = RoundedCornerShape(WatchStoreTheme.radius.large),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 104.dp, height = 126.dp)
                    .background(
                        WatchStoreTheme.colors.porcelain,
                        RoundedCornerShape(WatchStoreTheme.radius.large)
                    ),
            ) {
                WatchImage(
                    watch = watch,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                )
            }
            Spacer(Modifier.width(15.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = watch.name.replace("\n", " "),
                    style = MaterialTheme.typography.titleLarge,
                    color = WatchStoreTheme.colors.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = formatPrice(watch.priceCents * item.quantity),
                    style = MaterialTheme.typography.bodyLarge,
                    color = WatchStoreTheme.colors.cobalt,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(13.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WatchQuantityButton(
                        label = "−",
                        onClick = {
                            if (item.quantity > 1) onDecrease() else onRemove()
                        }
                    )
                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(36.dp),
                    )
                    WatchQuantityButton(
                        label = "+",
                        onClick = onIncrease
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.action_remove),
                        style = MaterialTheme.typography.labelSmall,
                        color = WatchStoreTheme.colors.mutedInk,
                        modifier = Modifier
                            .clip(RoundedCornerShape(WatchStoreTheme.radius.small))
                            .clickable(onClick = onRemove)
                            .padding(7.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun WatchQuantityButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                if (enabled) WatchStoreTheme.colors.porcelain else WatchStoreTheme.colors.porcelain.copy(
                    alpha = 0.5f
                ),
                CircleShape
            )
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (enabled) WatchStoreTheme.colors.ink else WatchStoreTheme.colors.ink.copy(
                alpha = 0.3f
            ),
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
