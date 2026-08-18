package com.brkckr.watchstore.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// room entity for cart items
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val watchId: String,
    val quantity: Int
)
