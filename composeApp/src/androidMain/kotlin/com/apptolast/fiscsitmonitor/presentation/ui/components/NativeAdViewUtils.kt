package com.apptolast.fiscsitmonitor.presentation.ui.components

/**
 * Jetpack Compose wrappers for Google Mobile Ads Native Advanced Ads.
 *
 * Pattern: [NativeAdView] wraps a real [com.google.android.gms.ads.nativead.NativeAdView]
 * (Android SDK) that in turn contains a [ComposeView]. Each asset wrapper composable
 * ([NativeAdHeadlineView], [NativeAdBodyView], etc.) creates a [ComposeView] internally and
 * registers itself with the parent [NativeAdView] via the appropriate setter, so the AdMob SDK
 * can track impressions and clicks correctly.
 *
 * Callers compose these inside a [NativeAdView] block and the [LocalNativeAdView]
 * CompositionLocal wires everything together transparently.
 *
 * Based on the official Google sample:
 * https://github.com/googleads/googleads-mobile-android-examples/blob/main/kotlin/advanced/
 * JetpackComposeDemo/app/src/main/java/com/google/android/gms/example/jetpackcomposedemo/
 * formats/compose_utils/NativeAdView.kt
 */

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * CompositionLocal that provides the nearest [NativeAdView] to asset composables so they can
 * register themselves with the SDK without explicit prop-drilling.
 */
internal val LocalNativeAdView = staticCompositionLocalOf<NativeAdView?> { null }

/**
 * Compose wrapper for [NativeAdView]. Places a real [NativeAdView] (required by the AdMob SDK
 * for click/impression tracking) and hosts the entire [content] tree inside a [ComposeView]
 * child. All asset wrappers declared inside [content] receive the [NativeAdView] reference via
 * [LocalNativeAdView] and register themselves accordingly.
 */
@Composable
fun NativeAdView(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val nativeAdViewRef = remember { mutableStateOf<NativeAdView?>(null) }

    AndroidView(
        factory = { context ->
            val composeView = ComposeView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            }
            NativeAdView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                addView(composeView)
                nativeAdViewRef.value = this
            }
        },
        modifier = modifier,
        update = { view ->
            val composeView = view.getChildAt(0) as? ComposeView
            composeView?.setContent {
                // Provide the NativeAdView to all asset wrappers in the subtree.
                CompositionLocalProvider(LocalNativeAdView provides view) { content() }
            }
        },
    )

    val currentNativeAd by rememberUpdatedState(nativeAd)
    SideEffect { nativeAdViewRef.value?.setNativeAd(currentNativeAd) }
}

/** Registers a [ComposeView] as [NativeAdView.headlineView]. */
@Composable
fun NativeAdHeadlineView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.headlineView = view
            view.setContent(content)
        },
    )
}

/** Registers a [ComposeView] as [NativeAdView.bodyView]. */
@Composable
fun NativeAdBodyView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.bodyView = view
            view.setContent(content)
        },
    )
}

/** Registers a [ComposeView] as [NativeAdView.iconView]. */
@Composable
fun NativeAdIconView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.iconView = view
            view.setContent(content)
        },
    )
}

/** Registers a [ComposeView] as [NativeAdView.callToActionView]. */
@Composable
fun NativeAdCallToActionView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.callToActionView = view
            view.setContent(content)
        },
    )
}

/** Registers a [ComposeView] as [NativeAdView.advertiserView]. */
@Composable
fun NativeAdAdvertiserView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.advertiserView = view
            view.setContent(content)
        },
    )
}

/** Registers a [ComposeView] as [NativeAdView.starRatingView]. */
@Composable
fun NativeAdStarRatingView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> ComposeView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.starRatingView = view
            view.setContent(content)
        },
    )
}

/** Registers a [MediaView] as [NativeAdView.mediaView]. */
@Composable
fun NativeAdMediaView(
    modifier: Modifier = Modifier,
    scaleType: ImageView.ScaleType? = null,
) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context -> MediaView(context) },
        modifier = modifier,
        update = { view ->
            nativeAdView.mediaView = view
            scaleType?.let { view.setImageScaleType(it) }
        },
    )
}

/** Registers an [AdChoicesView] as [NativeAdView.adChoicesView]. */
@Composable
fun NativeAdChoicesView(modifier: Modifier = Modifier) {
    val nativeAdView = LocalNativeAdView.current ?: return
    AndroidView(
        factory = { context ->
            AdChoicesView(context).apply {
                minimumWidth = 15
                minimumHeight = 15
            }
        },
        modifier = modifier,
        update = { view -> nativeAdView.adChoicesView = view },
    )
}

/**
 * Ad attribution badge (e.g. "Anuncio"). Pure Compose — not an asset view, just a visual label.
 */
@Composable
fun NativeAdAttribution(
    modifier: Modifier = Modifier,
    text: String = "Ad",
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    textStyle: TextStyle = TextStyle.Default,
    padding: PaddingValues = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
) {
    Box(modifier = modifier
        .background(containerColor, shape)
        .padding(padding)) {
        Text(text = text, color = contentColor, style = textStyle)
    }
}

/**
 * CTA button for native ads. Intentionally NOT a Compose [Button]: Compose buttons install their
 * own `pointerInput` click handlers which conflict with AdMob's native click tracking. Use this
 * [Box]+[Text] version instead; clicks are captured by the SDK via [NativeAdView.callToActionView]
 * and reported to [com.google.android.gms.ads.AdListener.onAdClicked].
 */
@Composable
fun NativeAdButton(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    textStyle: TextStyle = TextStyle.Default,
    padding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
) {
    Box(modifier = modifier
        .background(containerColor, shape)
        .padding(padding)) {
        Text(text = text, color = contentColor, style = textStyle)
    }
}
