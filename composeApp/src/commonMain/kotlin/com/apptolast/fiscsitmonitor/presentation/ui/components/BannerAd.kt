package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Google AdMob banner ad. Platform-specific:
 * - Android: renders a real AdView bound to BuildKonfig.ADMOB_BANNER_ID_ANDROID.
 * - iOS: renders a `BannerView` (Google Mobile Ads SDK v12+; the old `GAD` prefix was dropped)
 *        provided by a Swift factory injected via Koin. If the factory is not wired yet (e.g.
 *        running an old iosApp without the GoogleMobileAds SPM dependency), it renders an empty
 *        box so the layout doesn't break.
 *
 * Ad unit IDs are provided by BuildKonfig (read from local.properties). See README / CLAUDE.md
 * for setup instructions.
 */
@Composable
expect fun BannerAd(modifier: Modifier = Modifier)
