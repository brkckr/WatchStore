package com.brkckr.watchstore.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brkckr.watchstore.data.WatchRepository
import com.brkckr.watchstore.model.CartItem
import com.brkckr.watchstore.model.Watch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// viewmodel for managing cart state and operations
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: WatchRepository
) : ViewModel() {

    // list of items currently in the cart
    val cartItems: StateFlow<List<CartItem>> = repository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // total price of all items in the cart
    val cartTotalCents: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { item ->
            (repository.getWatchById(item.watchId)?.priceCents ?: 0) * item.quantity
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun increaseQuantity(watchId: String) {
        viewModelScope.launch {
            repository.increaseQuantity(watchId)
        }
    }

    fun decreaseQuantity(watchId: String) {
        viewModelScope.launch {
            repository.decreaseQuantity(watchId)
        }
    }

    fun removeItem(watchId: String) {
        viewModelScope.launch {
            repository.removeFromCart(watchId)
        }
    }

    fun getWatchById(watchId: String): Watch? {
        return repository.getWatchById(watchId)
    }
}
