package com.brkckr.watchstore.util

import java.text.NumberFormat
import java.util.Locale

// formats price in cents to currency string
fun formatPrice(cents: Int): String = NumberFormat
    .getCurrencyInstance(Locale.US)
    .format(cents / 100.0)
    .replace(".00", "")
