package com.brkckr.watchstore.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brkckr.watchstore.model.Watch
import com.brkckr.watchstore.ui.screens.cart.CartScreen
import com.brkckr.watchstore.ui.screens.watch.WatchContainer
import com.brkckr.watchstore.ui.theme.WatchStoreTheme
import com.brkckr.watchstore.ui.viewmodels.CartViewModel
import com.brkckr.watchstore.ui.viewmodels.WatchViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// root composable managing navigation and global animation state
@Composable
fun WatchStoreScreen(
    watchViewModel: WatchViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
) {
    val cartItems by watchViewModel.cartItems.collectAsState()
    val totalQuantity = cartItems.sumOf { it.quantity }

    var currentDestination by rememberSaveable { mutableStateOf(StoreDestination.WATCH) }
    var isDetailActive by rememberSaveable { mutableStateOf(value = false) }
    var selectedWatchIndex by rememberSaveable { mutableIntStateOf(0) }

    // animation state for the add to cart transition
    var cartAnimState by remember { mutableStateOf(CartAnimationStage.IDLE) }
    var itemBeingAdded by remember { mutableStateOf<Watch?>(null) }
    var isMorphingInDetail by remember { mutableStateOf(value = false) }

    val morphProgress by animateFloatAsState(
        targetValue = if (cartAnimState == CartAnimationStage.DROPPING) 1f else 0f,
        animationSpec = tween(
            durationMillis = WatchStoreTheme.motion.durationMedium,
            easing = WatchStoreTheme.motion.standardEasing,
        ),
        label = "cart-morph",
    )

    BackHandler(enabled = (currentDestination == StoreDestination.CART || isDetailActive)) {
        // handle system back based on current app state
        when {
            currentDestination == StoreDestination.CART -> currentDestination =
                StoreDestination.WATCH

            cartAnimState == CartAnimationStage.IDLE -> isDetailActive = false
        }
    }

    val onAddToCart: (Watch) -> Unit = { watch ->
        // initiate animation if idle and update data
        if (cartAnimState == CartAnimationStage.IDLE) {
            if (isDetailActive) {
                itemBeingAdded = watch
                isMorphingInDetail = true
                cartAnimState = CartAnimationStage.DROPPING
            }
            watchViewModel.addToCart(watch)
        }
    }

    LaunchedEffect(cartAnimState) {
        // manage the sequence of animation stages
        when (cartAnimState) {
            CartAnimationStage.DROPPING -> {
                if (isMorphingInDetail) {
                    delay(2350.milliseconds)
                    cartAnimState = CartAnimationStage.RETURNING
                }
            }

            CartAnimationStage.RETURNING -> {
                delay(650.milliseconds)
                cartAnimState = CartAnimationStage.IDLE
                itemBeingAdded = null
                isMorphingInDetail = false
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WatchStoreTheme.colors.porcelain)
    ) {
        // navigate between main watch feed and cart screen
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = {
                if (targetState == StoreDestination.CART) {
                    (slideInHorizontally(tween(460)) { it } + fadeIn(tween(260))) togetherWith
                            (slideOutHorizontally(tween(460)) { -it / 4 } + fadeOut(tween(210)))
                } else {
                    (slideInHorizontally(tween(460)) { -it / 3 } + fadeIn(tween(260))) togetherWith
                            (slideOutHorizontally(tween(460)) { it } + fadeOut(tween(210)))
                }.using(SizeTransform(clip = false))
            },
            label = "destination",
        ) { destination ->
            when (destination) {
                StoreDestination.WATCH -> WatchContainer(
                    watches = watchViewModel.watches,
                    selectedIndex = selectedWatchIndex,
                    isDetailMode = isDetailActive,
                    cartCount = totalQuantity,
                    isAddingToCart = cartAnimState != CartAnimationStage.IDLE,
                    cartMorphProgress = { morphProgress }, // pass as lambda to avoid recomposition
                    onWatchSelected = { selectedWatchIndex = it },
                    onDetailModeChanged = {
                        if (cartAnimState == CartAnimationStage.IDLE) isDetailActive = it
                    },
                    onOpenCart = {
                        if (cartAnimState == CartAnimationStage.IDLE) currentDestination =
                            StoreDestination.CART
                    },
                    onAddToCart = onAddToCart,
                    onCartPositioned = { /* no-op */ },
                )

                StoreDestination.CART -> {
                    val items by cartViewModel.cartItems.collectAsState()
                    val totalCents by cartViewModel.cartTotalCents.collectAsState()

                    CartScreen(
                        cartItems = items,
                        cartCount = items.sumOf { it.quantity },
                        cartTotalCents = totalCents,
                        getWatchById = cartViewModel::getWatchById,
                        onIncrease = cartViewModel::increaseQuantity,
                        onDecrease = cartViewModel::decreaseQuantity,
                        onRemove = cartViewModel::removeItem,
                        onBack = { currentDestination = StoreDestination.WATCH },
                    )
                }
            }
        }
    }
}

// navigation destinations for the app
enum class StoreDestination { WATCH, CART }

// stages for the add to cart animation sequence
enum class CartAnimationStage { IDLE, DROPPING, RETURNING }

