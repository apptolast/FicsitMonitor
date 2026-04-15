package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Google AdMob Native Advanced Ad. Platform-specific:
 * - Android: loads a [NativeAd] via [AdLoader] and renders a fully themed [NativeAdView] card
 *   inflated from res/layout/native_ad_card.xml, with all colors matching the app's dark palette.
 * - iOS: renders a native [NativeAdView] built in Swift (AdMobNativeAdFactory) and injected via
 *   Koin. Falls back to an empty UIView if the factory is not registered yet.
 *
 * Designed to be placed inline inside scrollable lists. The composable handles async loading
 * internally — nothing is shown while the ad is loading, and the loaded ad appears when ready.
 */
@Composable
expect fun NativeAd(modifier: Modifier = Modifier)
