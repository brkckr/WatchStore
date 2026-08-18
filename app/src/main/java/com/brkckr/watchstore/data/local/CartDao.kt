package com.brkckr.watchstore.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// data access object for cart operations
@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartItemEntity)

    @Delete
    suspend fun delete(item: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE watchId = :watchId")
    suspend fun getItemById(watchId: String): CartItemEntity?

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE watchId = :watchId")
    suspend fun incrementQuantity(watchId: String)

    @Query("UPDATE cart_items SET quantity = CASE WHEN quantity > 1 THEN quantity - 1 ELSE 1 END WHERE watchId = :watchId")
    suspend fun decrementQuantity(watchId: String)

    @Query("DELETE FROM cart_items WHERE watchId = :watchId")
    suspend fun deleteById(watchId: String)
}
