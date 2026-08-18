package com.brkckr.watchstore.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brkckr.watchstore.data.WatchRepository
import com.brkckr.watchstore.model.CartItem
import com.brkckr.watchstore.model.Watch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// viewmodel for managing watch data and basic cart actions
@HiltViewModel
class WatchViewModel @Inject constructor(
    private val repository: WatchRepository
) : ViewModel() {

    val watches = repository.getWatches()

    // flow of cart items for sync and badge updates
    val cartItems: StateFlow<List<CartItem>> = repository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addToCart(watch: Watch) {
        viewModelScope.launch {
            repository.addToCart(watch.id)
        }
    }
}
