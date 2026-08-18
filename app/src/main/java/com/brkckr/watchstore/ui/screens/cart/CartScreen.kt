package com.brkckr.watchstore.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.brkckr.watchstore.R
import com.brkckr.watchstore.model.CartItem
import com.brkckr.watchstore.model.Watch
import com.brkckr.watchstore.ui.components.CartTopBar
import com.brkckr.watchstore.ui.components.WatchStoreButton
import com.brkckr.watchstore.ui.theme.WatchStoreTheme

// screen displaying cart items, total and checkout option
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    cartCount: Int,
    cartTotalCents: Int,
    getWatchById: (String) -> Watch?,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        CartTopBar(cartCount = cartCount, onBack = onBack)

        if (cartItems.isEmpty()) {
            EmptyCart(onContinue = onBack)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    top = 12.dp,
                    end = 18.dp,
                    bottom = 18.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                items(cartItems, key = { it.watchId }) { item ->
                    getWatchById(item.watchId)?.let { watch ->
                        CartLineItem(
                            watch = watch,
                            item = item,
                            onIncrease = { onIncrease(item.watchId) },
                            onDecrease = { onDecrease(item.watchId) },
                            onRemove = { onRemove(item.watchId) },
                        )
                    }
                }
            }

            CartSummary(
                totalCents = cartTotalCents,
                onCheckout = { /* no action per request */ },
            )
        }
    }
}

// state shown when the cart has no items
@Composable
private fun EmptyCart(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            stringResource(R.string.cart_empty_title),
            style = MaterialTheme.typography.displaySmall,
            color = WatchStoreTheme.colors.ink
        )
        Spacer(Modifier.height(9.dp))
        Text(
            stringResource(R.string.cart_empty_message),
            style = MaterialTheme.typography.bodyMedium,
            color = WatchStoreTheme.colors.mutedInk,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(30.dp))
        WatchStoreButton(text = stringResource(R.string.action_explore), onClick = onContinue)
    }
}
