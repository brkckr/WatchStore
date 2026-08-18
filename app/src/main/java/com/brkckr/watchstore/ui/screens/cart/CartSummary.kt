package com.brkckr.watchstore.ui.screens.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brkckr.watchstore.R
import com.brkckr.watchstore.ui.components.WatchStoreButton
import com.brkckr.watchstore.ui.theme.WatchStoreTheme
import com.brkckr.watchstore.util.formatPrice

// bottom sheet style summary of cart totals
@Composable
fun CartSummary(totalCents: Int, onCheckout: () -> Unit) {
    Surface(
        color = WatchStoreTheme.colors.paper,
        shape = RoundedCornerShape(
            topStart = WatchStoreTheme.radius.extraLarge,
            topEnd = WatchStoreTheme.radius.extraLarge,
        ),
        shadowElevation = 18.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    start = 25.dp,
                    top = 22.dp,
                    end = 25.dp,
                    bottom = 16.dp
                )
        ) {
            SummaryRow(label = stringResource(R.string.summary_subtotal), value = formatPrice(totalCents))
            Spacer(Modifier.height(9.dp))
            SummaryRow(label = stringResource(R.string.summary_delivery), value = stringResource(R.string.summary_delivery_free), compact = true)
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = WatchStoreTheme.colors.porcelain)
            Spacer(Modifier.height(15.dp))
            SummaryRow(label = stringResource(R.string.summary_total), value = formatPrice(totalCents), emphasized = true)
            Spacer(Modifier.height(18.dp))
            WatchStoreButton(text = stringResource(R.string.action_checkout), onClick = onCheckout)
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    emphasized: Boolean = false,
    compact: Boolean = false,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = if (emphasized) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium,
            color = if (emphasized) WatchStoreTheme.colors.ink else WatchStoreTheme.colors.mutedInk,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = when {
                emphasized -> MaterialTheme.typography.titleLarge
                compact -> MaterialTheme.typography.labelSmall
                else -> MaterialTheme.typography.bodyMedium
            },
            color = if (emphasized) WatchStoreTheme.colors.cobalt else WatchStoreTheme.colors.ink,
            fontWeight = if (emphasized) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}
