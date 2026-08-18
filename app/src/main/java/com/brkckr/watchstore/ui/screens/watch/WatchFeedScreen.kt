package com.brkckr.watchstore.ui.screens.watch

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brkckr.watchstore.R
import com.brkckr.watchstore.model.Watch
import com.brkckr.watchstore.ui.components.WatchImage
import com.brkckr.watchstore.ui.components.WatchSilhouette
import com.brkckr.watchstore.ui.components.WatchStoreButton
import com.brkckr.watchstore.ui.theme.WatchStoreTheme
import com.brkckr.watchstore.util.formatPrice
import kotlin.math.abs

// vertical scrollable feed of watches with parallax effects
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchFeedScreen(
    watches: List<Watch>,
    selectedIndex: Int,
    isAddingToCart: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onWatchSelected: (Int) -> Unit,
    onOpenDetail: () -> Unit,
    onAddToCart: (Watch) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = selectedIndex) { watches.size }

    LaunchedEffect(pagerState.currentPage) {
        onWatchSelected(pagerState.currentPage)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeightPx = constraints.maxHeight.toFloat()

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = !isAddingToCart,
            beyondViewportPageCount = 1, // pre-rendering next page prevents stuttering at start of scroll
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = tween(
                    durationMillis = WatchStoreTheme.motion.durationLong,
                    easing = WatchStoreTheme.motion.standardEasing,
                )
            )
        ) { page ->
            val watch = watches[page]
            val rotationZ = watchRotation(page)

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(82.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    WatchSilhouette(
                        watch = watch,
                        rotationZ = rotationZ,
                        blurRadius = 8.dp,
                        modifier = Modifier
                            .fillMaxSize(0.85f)
                            .graphicsLayer {
                                val pageOffset = pagerState.getOffsetDistanceInPages(page)
                                val absOffset = abs(pageOffset)
                                
                                // increased parallax speed for the background layer
                                translationY = -pageOffset * screenHeightPx * 0.65f
                                alpha = (1f - (absOffset * 2.5f)).coerceIn(0f, 1f)
                                scaleX = 1f + (absOffset * 0.15f)
                                scaleY = 1f + (absOffset * 0.15f)
                            }
                    )

                    with(sharedTransitionScope) {
                        WatchImage(
                            watch = watch,
                            modifier = Modifier
                                .fillMaxSize(0.85f)
                                .sharedElement(
                                    rememberSharedContentState(key = "watch-image-${watch.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    zIndexInOverlay = 10f
                                )
                                .graphicsLayer {
                                    val pageOffset = pagerState.getOffsetDistanceInPages(page)
                                    val absOffset = abs(pageOffset)

                                    // decreased parallax speed for the foreground to increase speed gap
                                    translationY = (pageOffset * screenHeightPx) * 0.08f
                                    this.rotationZ = rotationZ
                                    rotationX = pageOffset * 25f
                                    val baseScale = 1f - (absOffset * 0.20f)
                                    scaleX = baseScale
                                    scaleY = baseScale
                                    alpha = (1f - (absOffset * 2.5f)).coerceIn(0f, 1f)
                                }
                                .clickable(
                                    enabled = true,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onOpenDetail
                                ),
                            crossfadeEnabled = false
                        )
                    }
                }

                WatchFeedInfoCard(
                    watch = watch,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onAddToCart = { onAddToCart(watch) },
                    modifier = Modifier.graphicsLayer {
                        // move state reading into graphicsLayer to avoid recomposition
                        val pageOffset = pagerState.getOffsetDistanceInPages(page)
                        val absOffset = abs(pageOffset)

                        translationY = pageOffset * screenHeightPx * 0.45f
                        alpha = (1f - (absOffset * 2f)).coerceIn(0f, 1f)
                    }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchFeedInfoCard(
    watch: Watch,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(horizontal = 18.dp)
    ) {
        val browseButtonHeight = 60.dp

        with(sharedTransitionScope) {
            Surface(
                color = WatchStoreTheme.colors.paper,
                shape = RoundedCornerShape(WatchStoreTheme.radius.medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp - (browseButtonHeight / 2))
                    .sharedElement(
                        rememberSharedContentState(key = "watch-card-${watch.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 6f
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = watch.eyebrow,
                        style = WatchStoreTheme.typography.specLabel,
                        color = WatchStoreTheme.colors.mutedInk,
                        letterSpacing = 1.8.sp,
                    )
                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = watch.name.replace("\n", " "),
                        style = MaterialTheme.typography.headlineMedium,
                        color = WatchStoreTheme.colors.ink,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "watch-name-${watch.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 6f
                        )
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = formatPrice(watch.priceCents),
                        style = WatchStoreTheme.typography.price,
                        color = WatchStoreTheme.colors.mutedInk,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "watch-price-${watch.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 6f
                        )
                    )

                    Spacer(Modifier.weight(1f))
                }
            }

            WatchStoreButton(
                text = stringResource(R.string.action_add_to_cart),
                onClick = onAddToCart,
                height = browseButtonHeight,
                cornerRadius = WatchStoreTheme.radius.medium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.70f)
                    .sharedElement(
                        rememberSharedContentState(key = "watch-button-${watch.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 7f
                    )
            )
        }
    }
}

private fun watchRotation(index: Int): Float = if ((index % 2) == 0) -30f else 30f
