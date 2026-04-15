package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.apptolast.fiscsitmonitor.ads.AdIds
import com.apptolast.fiscsitmonitor.presentation.ui.theme.AccentGreen
import com.apptolast.fiscsitmonitor.presentation.ui.theme.AccentGreen20
import com.apptolast.fiscsitmonitor.presentation.ui.theme.BgCard
import com.apptolast.fiscsitmonitor.presentation.ui.theme.Border
import com.apptolast.fiscsitmonitor.presentation.ui.theme.TextOnAccent
import com.apptolast.fiscsitmonitor.presentation.ui.theme.TextPrimary
import com.apptolast.fiscsitmonitor.presentation.ui.theme.TextSecondary
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

@Composable
actual fun NativeAd(modifier: Modifier) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val context = LocalContext.current
    var isDisposed by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        AdLoader.Builder(context, AdIds.nativeIdAndroid)
            .forNativeAd { ad ->
                if (!isDisposed) nativeAd = ad else ad.destroy()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Composable stays invisible — no crash, no layout shift.
                }
            })
            .build()
            .loadAd(AdRequest.Builder().build())

        onDispose {
            isDisposed = true
            nativeAd?.destroy()
            nativeAd = null
        }
    }

    nativeAd?.let { ad -> FicsitNativeAdCard(ad, modifier) }
}

/**
 * Renders a loaded [NativeAd] as a compact inline card that matches the FicsitMonitor dark
 * palette. Layout: attribution badge → icon + headline/body + CTA button.
 *
 * Uses [NativeAdView] (the Compose wrapper from [NativeAdViewUtils.kt]) which hosts a real
 * [com.google.android.gms.ads.nativead.NativeAdView] so AdMob can track impressions and clicks.
 * Each asset ([NativeAdHeadlineView], [NativeAdBodyView], etc.) registers itself via the SDK
 * setter; [NativeAdButton] is intentionally NOT a Compose Button to avoid click-handler conflicts.
 */
@Composable
private fun FicsitNativeAdCard(nativeAd: NativeAd, modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(12.dp)

    // The NativeAdView must cover the FULL card area (including inner padding) so that the
    // AdMob SDK sees all registered asset views inside its boundaries. Padding goes on the
    // inner Column, NOT on the NativeAdView modifier.
    NativeAdView(
        nativeAd = nativeAd,
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(BgCard)
            .border(1.dp, Border, cardShape),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            // Ad attribution badge — pure Compose, not an asset view.
            NativeAdAttribution(
                text = "Anuncio",
                shape = RoundedCornerShape(4.dp),
                containerColor = AccentGreen20,
                contentColor = AccentGreen,
                textStyle = MaterialTheme.typography.labelSmall,
                padding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Icon (optional asset)
                nativeAd.icon?.let { icon ->
                    NativeAdIconView(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    ) {
                        icon.drawable?.toBitmap()?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }

                // Headline + body
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    nativeAd.headline?.let { headline ->
                        NativeAdHeadlineView {
                            androidx.compose.material3.Text(
                                text = headline,
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    nativeAd.body?.let { body ->
                        NativeAdBodyView {
                            androidx.compose.material3.Text(
                                text = body,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                // CTA button (optional asset) — NativeAdButton, NOT Compose Button.
                nativeAd.callToAction?.let { cta ->
                    NativeAdCallToActionView {
                        NativeAdButton(
                            text = cta,
                            shape = RoundedCornerShape(6.dp),
                            containerColor = AccentGreen,
                            contentColor = TextOnAccent,
                            textStyle = MaterialTheme.typography.labelSmall,
                            padding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        }
    }
}
