package com.brkckr.watchstore.data

import com.brkckr.watchstore.data.local.CartDao
import com.brkckr.watchstore.data.local.CartItemEntity
import com.brkckr.watchstore.model.CartItem
import com.brkckr.watchstore.model.Watch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// repository interface for watch and cart data
interface WatchRepository {
    fun getWatches(): List<Watch>
    fun getWatchById(id: String): Watch?
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(watchId: String)
    suspend fun removeFromCart(watchId: String)
    suspend fun increaseQuantity(watchId: String)
    suspend fun decreaseQuantity(watchId: String)
}

// repository implementation using room and static data
@Singleton
class WatchRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : WatchRepository {

    override fun getWatches(): List<Watch> = WatchDataSource.watches

    override fun getWatchById(id: String): Watch? = WatchDataSource.getWatchById(id)

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllItems().map { entities ->
            entities.map { CartItem(it.watchId, it.quantity) }
        }
    }

    override suspend fun addToCart(watchId: String) {
        // logic to add or increment item in cart
        val existing = cartDao.getItemById(watchId)
        if (existing == null) {
            cartDao.insertOrUpdate(CartItemEntity(watchId, 1))
        } else {
            cartDao.incrementQuantity(watchId)
        }
    }

    override suspend fun removeFromCart(watchId: String) {
        cartDao.deleteById(watchId)
    }

    override suspend fun increaseQuantity(watchId: String) {
        cartDao.incrementQuantity(watchId)
    }

    override suspend fun decreaseQuantity(watchId: String) {
        cartDao.decrementQuantity(watchId)
    }
}
