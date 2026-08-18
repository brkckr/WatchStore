package com.brkckr.watchstore.ui.screens.watch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.brkckr.watchstore.model.Watch
import com.brkckr.watchstore.ui.components.WatchTopBar

// container orchestrating transition between feed and detail views
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchContainer(
    watches: List<Watch>,
    selectedIndex: Int,
    isDetailMode: Boolean,
    cartCount: Int,
    isAddingToCart: Boolean,
    cartMorphProgress: () -> Float,
    onWatchSelected: (Int) -> Unit,
    onDetailModeChanged: (Boolean) -> Unit,
    onOpenCart: () -> Unit,
    onAddToCart: (Watch) -> Unit,
    onCartPositioned: (Offset) -> Unit,
) {
    SharedTransitionLayout {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = isDetailMode,
                transitionSpec = {
                    if (targetState) {
                        (fadeIn(tween(400)) + slideInVertically { it / 8 }).togetherWith(
                            fadeOut(
                                tween(300),
                            )
                        )
                    } else {
                        fadeIn(tween(400)).togetherWith(fadeOut(tween(300)) + slideOutVertically { it / 8 })
                    }.using(SizeTransform(clip = false))
                },
                label = "screen-transition"
            ) { detailed ->
                if (!detailed) {
                    WatchFeedScreen(
                        watches = watches,
                        selectedIndex = selectedIndex,
                        isAddingToCart = isAddingToCart,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onWatchSelected = onWatchSelected,
                        onOpenDetail = { onDetailModeChanged(true) },
                        onAddToCart = onAddToCart
                    )
                } else {
                    WatchDetailScreen(
                        watch = watches[selectedIndex],
                        isAddingToCart = isAddingToCart,
                        cartMorphProgress = cartMorphProgress,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onAddToCart = onAddToCart
                    )
                }
            }

            // top bar placed outside to stay fixed, but internal elements 
            // respond to detailed mode to hide/show actions
            WatchTopBar(
                showBack = isDetailMode,
                cartCount = cartCount,
                onBack = { onDetailModeChanged(false) },
                onOpenCart = onOpenCart,
                onCartPositioned = onCartPositioned,
                modifier = Modifier
                    .statusBarsPadding()
                    .graphicsLayer {
                        // total bar fades out only during cart morph sequence in detail
                        alpha = if (isDetailMode) 1f - cartMorphProgress() else 1f
                    }
                    .zIndex(100f)
            )
        }
    }
}
