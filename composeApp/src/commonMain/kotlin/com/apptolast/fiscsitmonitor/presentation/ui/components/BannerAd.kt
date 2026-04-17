package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Google AdMob Standard Banner Ad (320x50). Platform-specific:
 * - Android: loads a [com.google.android.gms.ads.AdView] via [AndroidView].
 * - iOS: delegates to [com.apptolast.fiscsitmonitor.ads.IosBannerAdFactory] implemented in Swift,
 *   wrapping a [BannerView] in a [UIKitView].
 *
 * Intended to be placed at the bottom of a screen (e.g. WebViewerScreen) as a monetization strip.
 */
@Composable
expect fun BannerAd(modifier: Modifier = Modifier)
