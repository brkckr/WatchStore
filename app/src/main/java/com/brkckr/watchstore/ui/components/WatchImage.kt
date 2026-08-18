package com.brkckr.watchstore.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.brkckr.watchstore.model.Watch

// loads and displays watch artwork using coil
@Composable
fun WatchImage(
    modifier: Modifier = Modifier,
    watch: Watch,
    imageUrl: String = watch.artworkUrl,
    contentDescription: String? = watch.name.replace("\n", " "),
    colorFilter: ColorFilter? = null,
    crossfadeEnabled: Boolean = true,
) {
    val context = LocalContext.current
    val imageRequest = remember(context, imageUrl, crossfadeEnabled) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(crossfadeEnabled)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        colorFilter = colorFilter,
        modifier = modifier,
    )
}

// displays a blurred watch silhouette for visual depth
@Composable
fun WatchSilhouette(
    watch: Watch,
    rotationZ: Float,
    modifier: Modifier = Modifier,
    imageUrl: String = watch.alternateArtworkUrl, // Defaults to alternate, but can be overridden
    alpha: Float = 0.60f,
    blurRadius: Dp = 2.dp,
) {
    val density = LocalDensity.current
    val silhouetteOffset = with(density) { 20.dp.toPx() }
    val silhouetteVerticalOffset = with(density) { 8.dp.toPx() }

    WatchImage(
        watch = watch,
        imageUrl = imageUrl,
        contentDescription = null,
        colorFilter = null,
        crossfadeEnabled = false,
        modifier = modifier
            .graphicsLayer {
                this.rotationZ = -1.5f * rotationZ
                scaleX = 0.96f
                scaleY = 0.96f
                this.alpha = alpha

                val offsetFactor = (rotationZ / 30f).coerceIn(-1f, 1f)
                translationX = -offsetFactor * silhouetteOffset
                translationY = silhouetteVerticalOffset
            }
            .blur(blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
    )
}
