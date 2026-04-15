package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import com.apptolast.fiscsitmonitor.ads.AdIds
import com.apptolast.fiscsitmonitor.ads.IosNativeAdFactory
import org.koin.mp.KoinPlatform
import platform.UIKit.UIView

@Composable
actual fun NativeAd(modifier: Modifier) {
    // If the Swift factory hasn't been registered yet (e.g. running without GoogleMobileAds
    // wired up), fall back to an empty UIView so the layout doesn't break.
    val factory: IosNativeAdFactory? = remember {
        runCatching { KoinPlatform.getKoin().get<IosNativeAdFactory>() }.getOrNull()
    }
    UIKitView(
        factory = {
            factory?.createNativeAdView(AdIds.nativeIdIos) ?: UIView()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
    )
}
