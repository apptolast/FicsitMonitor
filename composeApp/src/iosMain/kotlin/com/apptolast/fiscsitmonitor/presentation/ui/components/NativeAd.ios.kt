package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import com.apptolast.fiscsitmonitor.ads.AdIds
import com.apptolast.fiscsitmonitor.ads.IosNativeAdFactory
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.mp.KoinPlatform
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeAd(modifier: Modifier) {
    // If the Swift factory hasn't been registered yet (e.g. running without GoogleMobileAds
    // wired up), fall back to an empty UIView so the layout doesn't break.
    val factory: IosNativeAdFactory? = remember {
        runCatching { KoinPlatform.getKoin().get<IosNativeAdFactory>() }.getOrNull()
    }
    // background = Color.Transparent makes the UIKitView host UIView transparent. Without this
    // the host UIView defaults to white, which bleeds through the transparent corners of the
    // AdContainerView (UIKit's cornerRadius clips the layer to a rounded rect, leaving the
    // rectangle corners outside the radius as transparent).
    UIKitView(
        factory = {
            factory?.createNativeAdView(AdIds.nativeIdIos) ?: UIView()
        },
        modifier = modifier
            .background(color = Color.Transparent)
            .fillMaxWidth()
            .height(100.dp),
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}
