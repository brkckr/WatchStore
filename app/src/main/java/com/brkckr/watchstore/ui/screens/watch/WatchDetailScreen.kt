package com.brkckr.watchstore.ui.screens.watch

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.brkckr.watchstore.R
import com.brkckr.watchstore.model.Watch
import com.brkckr.watchstore.util.formatPrice
import com.brkckr.watchstore.ui.components.WatchImage
import com.brkckr.watchstore.ui.components.WatchSilhouette
import com.brkckr.watchstore.ui.components.WatchStoreButton
import com.brkckr.watchstore.ui.theme.WatchStoreTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.unit.lerp as dpLerp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchDetailScreen(
    watch: Watch,
    isAddingToCart: Boolean,
    cartMorphProgress: () -> Float,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAddToCart: (Watch) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0) { watch.images.size }
    val coroutineScope = rememberCoroutineScope()
    var photoIndex by rememberSaveable(watch.id) { mutableIntStateOf(0) }
    LaunchedEffect(pagerState.currentPage) { photoIndex = pagerState.currentPage }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(WatchStoreTheme.colors.porcelain)) {
        val metrics = rememberDetailMetrics(cartMorphProgress, maxHeight)

        ActionPanelLayer(
            watch = watch,
            metrics = metrics,
            isAddingToCart = isAddingToCart,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            onAddToCart = onAddToCart,
        )

        InfoCardLayer(
            watch = watch,
            metrics = metrics,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )

        if (isAddingToCart) {
            SilhouetteLayer(
                watch = watch,
                imageUrl = watch.artworkUrl,
                cartMorphProgress = cartMorphProgress,
                maxHeight = maxHeight,
                watchAreaHeight = metrics.watchAreaHeight
            )
        }

        HeroLayer(
            watch = watch,
            photoIndex = photoIndex,
            pagerState = pagerState,
            metrics = metrics,
            cartMorphProgress = cartMorphProgress,
            isAddingToCart = isAddingToCart,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            onPageSelected = { index ->
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            },
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun InfoCardLayer(
    watch: Watch,
    metrics: DetailMetrics,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Surface(
            color = WatchStoreTheme.colors.paper,
            shape = RoundedCornerShape(
                topStart = WatchStoreTheme.radius.extraLarge,
                topEnd = WatchStoreTheme.radius.extraLarge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val h = metrics.infoCardHeight().roundToPx()
                    val p = measurable.measure(constraints.copy(minHeight = h, maxHeight = h))
                    layout(p.width, p.height) { p.place(0, 0) }
                }
                .offset { IntOffset(0, metrics.infoCardTop().roundToPx()) }
                .sharedElement(
                    rememberSharedContentState(key = "watch-card-${watch.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    zIndexInOverlay = 6f
                )
                .zIndex(6f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.action_added_to_cart),
                    style = WatchStoreTheme.typography.action,
                    color = WatchStoreTheme.colors.ink,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 15.dp)
                        .graphicsLayer { alpha = metrics.confirmationAlpha() }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 88.dp, start = 24.dp, end = 24.dp, bottom = 50.dp)
                        .graphicsLayer { alpha = metrics.detailsAlpha() },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = watch.name.replace("\n", " "),
                        style = MaterialTheme.typography.headlineMedium,
                        color = WatchStoreTheme.colors.ink,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "watch-name-${watch.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 6f
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = watch.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = WatchStoreTheme.colors.mutedInk,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        DetailSpecItem(stringResource(R.string.spec_movement), watch.movement, Modifier.weight(1f))
                        DetailSpecItem(stringResource(R.string.spec_case), watch.caseSize, Modifier.weight(1f))
                        DetailSpecItem(stringResource(R.string.spec_water), watch.waterResistance, Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ActionPanelLayer(
    watch: Watch,
    metrics: DetailMetrics,
    isAddingToCart: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAddToCart: (Watch) -> Unit
) {
    val actionStyle = WatchStoreTheme.typography.action
    val paperColor = WatchStoreTheme.colors.paper

    with(sharedTransitionScope) {
        WatchStoreButton(
            text = stringResource(R.string.action_add_to_cart),
            onClick = { onAddToCart(watch) },
            height = 56.dp, // base height, but we override with layout
            enabled = !isAddingToCart,
            shape = RoundedCornerShape(
                topStart = metrics.panelCornerRadius(),
                topEnd = metrics.panelCornerRadius()
            ),
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val h = metrics.panelHeight().roundToPx()
                    val p = measurable.measure(constraints.copy(minHeight = h, maxHeight = h))
                    layout(p.width, p.height) { p.place(0, 0) }
                }
                .offset { IntOffset(0, metrics.panelTop().roundToPx()) }
                .sharedElement(
                    rememberSharedContentState(key = "watch-button-${watch.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    zIndexInOverlay = 7f
                )
                .zIndex(7f),
            content = {
                Text(
                    text = stringResource(R.string.action_add_to_cart),
                    style = actionStyle,
                    color = paperColor,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .navigationBarsPadding()
                        .graphicsLayer { alpha = metrics.actionAlpha() }
                )
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BoxScope.HeroLayer(
    watch: Watch,
    photoIndex: Int,
    pagerState: PagerState,
    metrics: DetailMetrics,
    cartMorphProgress: () -> Float,
    isAddingToCart: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPageSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.watchAreaHeight)
            .statusBarsPadding()
            .offset(y = 30.dp)
            .graphicsLayer {
                translationY = metrics.watchTranslationY().toPx()
                scaleX = metrics.watchScale()
                scaleY = metrics.watchScale()
                alpha = 1f 
            }
            .zIndex(10f),
        contentAlignment = Alignment.Center
    ) {
        with(sharedTransitionScope) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().sharedElement(
                    rememberSharedContentState(key = "watch-image-${watch.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    zIndexInOverlay = 10f
                ),
                userScrollEnabled = !isAddingToCart
            ) { page ->
                WatchImage(watch = watch, imageUrl = watch.images[page], modifier = Modifier.fillMaxSize(), crossfadeEnabled = false)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .offset { IntOffset(0, metrics.priceTop().roundToPx()) }
            .zIndex(15f),
        contentAlignment = Alignment.Center
    ) {
        with(sharedTransitionScope) {
            Text(
                text = formatPrice(watch.priceCents),
                style = WatchStoreTheme.typography.price,
                color = lerpColor(WatchStoreTheme.colors.mutedInk, Color.White, cartMorphProgress()),
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = "watch-price-${watch.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    zIndexInOverlay = 6f
                )
            )
        }
    }

    WatchGalleryDots(
        imageCount = watch.images.size,
        selectedIndex = photoIndex,
        onSelected = onPageSelected,
        modifier = Modifier.align(Alignment.TopCenter).offset(y = metrics.detailCardTop + 60.dp).graphicsLayer { alpha = metrics.detailsAlpha() }.zIndex(14f)
    )
}

@Composable
private fun WatchGalleryDots(
    imageCount: Int,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(imageCount) { index ->
            Box(modifier = Modifier.size(20.dp).clickable { onSelected(index) }, contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier.size(if (selectedIndex == index) 10.dp else 8.dp).background(
                        color = if (selectedIndex == index) WatchStoreTheme.colors.cobalt else WatchStoreTheme.colors.mutedInk.copy(
                            alpha = 0.35f
                        ), shape = CircleShape
                    )
                )
            }
        }
    }
}

@Composable
private fun SilhouetteLayer(
    watch: Watch,
    imageUrl: String,
    cartMorphProgress: () -> Float,
    maxHeight: androidx.compose.ui.unit.Dp,
    watchAreaHeight: androidx.compose.ui.unit.Dp
) {
    Box(modifier = Modifier.fillMaxSize().zIndex(11f), contentAlignment = Alignment.TopCenter) {
        WatchSilhouette(
            watch = watch,
            rotationZ = 0f,
            imageUrl = imageUrl,
            alpha = 0.4f,
            modifier = Modifier
                .fillMaxWidth()
                .height(watchAreaHeight)
                .offset {
                    val progress = cartMorphProgress()
                    val targetY = (maxHeight / 4f) - (watchAreaHeight / 2f)
                    IntOffset(0, dpLerp((-200).dp, targetY, progress).roundToPx())
                }
                .graphicsLayer { 
                    rotationZ = 30f 
                    alpha = (cartMorphProgress() * 2f).coerceIn(0f, 1f) * 0.4f
                }
        )
    }
}

@Composable
private fun DetailSpecItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = WatchStoreTheme.typography.specLabel, color = WatchStoreTheme.colors.mutedInk)
        Spacer(Modifier.height(4.dp))
        Text(text = value, style = WatchStoreTheme.typography.specValue, color = WatchStoreTheme.colors.ink, maxLines = 1)
    }
}

private data class DetailMetrics(
    val watchAreaHeight: androidx.compose.ui.unit.Dp,
    val detailCardTop: androidx.compose.ui.unit.Dp,
    val panelHeight: () -> androidx.compose.ui.unit.Dp,
    val infoCardHeight: () -> androidx.compose.ui.unit.Dp,
    val panelTop: () -> androidx.compose.ui.unit.Dp,
    val panelCornerRadius: () -> androidx.compose.ui.unit.Dp,
    val infoCardTop: () -> androidx.compose.ui.unit.Dp,
    val watchTranslationY: () -> androidx.compose.ui.unit.Dp,
    val watchScale: () -> Float,
    val priceTop: () -> androidx.compose.ui.unit.Dp,
    val detailsAlpha: () -> Float,
    val confirmationAlpha: () -> Float,
    val actionAlpha: () -> Float
)

@Composable
private fun rememberDetailMetrics(cartMorphProgress: () -> Float, maxHeight: androidx.compose.ui.unit.Dp): DetailMetrics {
    val radiusLarge = WatchStoreTheme.radius.large
    val radiusExtraLarge = WatchStoreTheme.radius.extraLarge

    return remember(maxHeight, radiusLarge, radiusExtraLarge) {
        val watchAreaH = maxHeight * 0.6f
        val cardTop = watchAreaH - 60.dp
        val actionH = 82.dp
        val expandedPanelH = maxHeight * 0.42f
        val expandedPanelT = maxHeight - expandedPanelH
        val cHeaderH = 48.dp
        val idleH = (maxHeight - (actionH + cardTop - 50.dp)).coerceAtLeast(0.dp)
        val targetH = cHeaderH + 50.dp 

        DetailMetrics(
            watchAreaHeight = watchAreaH,
            detailCardTop = cardTop,
            panelHeight = { dpLerp(actionH, expandedPanelH, cartMorphProgress()) },
            infoCardHeight = { dpLerp(idleH, targetH, cartMorphProgress()) },
            panelTop = { dpLerp(maxHeight - actionH, expandedPanelT, cartMorphProgress()) },
            panelCornerRadius = { dpLerp(radiusLarge, radiusExtraLarge, cartMorphProgress()) },
            infoCardTop = { dpLerp(cardTop, expandedPanelT - cHeaderH, cartMorphProgress()) },
            watchTranslationY = { 
                val watchStartCY = (watchAreaH / 2f) + 30.dp
                val watchTargetCY = (expandedPanelT + (expandedPanelH / 2f)) - 64.dp
                dpLerp(0.dp, watchTargetCY - watchStartCY, cartMorphProgress()) 
            },
            watchScale = { androidx.compose.ui.util.lerp(1f, 0.6f, cartMorphProgress()) },
            priceTop = {
                val priceStartCY = cardTop + 340.dp
                val watchTargetCY = (expandedPanelT + (expandedPanelH / 2f)) - 64.dp
                val priceTargetCY = watchTargetCY + 215.dp
                dpLerp(priceStartCY - 24.dp, priceTargetCY - 24.dp, cartMorphProgress())
            },
            detailsAlpha = { (1f - cartMorphProgress() * 3.5f).coerceIn(0f, 1f) },
            confirmationAlpha = { ((cartMorphProgress() - 0.4f) / 0.6f).coerceIn(0f, 1f) },
            actionAlpha = { (1f - cartMorphProgress() * 5f).coerceIn(0f, 1f) }
        )
    }
}
