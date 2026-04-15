package com.apptolast.fiscsitmonitor.presentation.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.apptolast.fiscsitmonitor.R
import com.apptolast.fiscsitmonitor.ads.AdIds
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
actual fun NativeAd(modifier: Modifier) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val loader = AdLoader.Builder(context, AdIds.nativeIdAndroid)
            .forNativeAd { ad -> nativeAd = ad }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Leave nativeAd as null — composable stays invisible, no crash.
                }
            })
            .build()
        loader.loadAd(AdRequest.Builder().build())
        onDispose { nativeAd?.destroy() }
    }

    nativeAd?.let { ad ->
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { ctx ->
                val adView = LayoutInflater.from(ctx)
                    .inflate(R.layout.native_ad_card, null, false) as NativeAdView
                adView.populateFrom(ad)
                adView
            },
        )
    }
}

/**
 * Populates a [NativeAdView] inflated from `native_ad_card.xml` with the assets of a loaded
 * [NativeAd]. Each asset view must be registered with [NativeAdView] so the SDK can track
 * impressions and handle clicks correctly.
 */
private fun NativeAdView.populateFrom(ad: NativeAd) {
    val iconView = findViewById<ImageView>(R.id.ad_icon)
    val headlineView = findViewById<TextView>(R.id.ad_headline)
    val bodyView = findViewById<TextView>(R.id.ad_body)
    val ctaView = findViewById<Button>(R.id.ad_cta)

    // Register asset views with the NativeAdView (required by AdMob SDK).
    this.iconView = iconView
    this.headlineView = headlineView
    this.bodyView = bodyView
    this.callToActionView = ctaView

    // Populate headline (always present in native ads).
    headlineView.text = ad.headline

    // Populate body (optional).
    if (ad.body != null) {
        bodyView.visibility = View.VISIBLE
        bodyView.text = ad.body
    } else {
        bodyView.visibility = View.GONE
    }

    // Populate icon (optional).
    val icon = ad.icon
    if (icon != null) {
        iconView.visibility = View.VISIBLE
        iconView.setImageDrawable(icon.drawable)
    } else {
        iconView.visibility = View.GONE
    }

    // Populate CTA (optional).
    if (ad.callToAction != null) {
        ctaView.visibility = View.VISIBLE
        ctaView.text = ad.callToAction
    } else {
        ctaView.visibility = View.GONE
    }

    // Bind ad data to the view (required — enables SDK click tracking).
    setNativeAd(ad)
}
