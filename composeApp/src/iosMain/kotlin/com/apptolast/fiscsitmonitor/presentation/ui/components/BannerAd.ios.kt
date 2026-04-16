package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import com.apptolast.fiscsitmonitor.ads.AdIds
import com.apptolast.fiscsitmonitor.ads.IosBannerAdFactory
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.mp.KoinPlatform
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BannerAd(modifier: Modifier) {
    val factory: IosBannerAdFactory? = remember {
        runCatching { KoinPlatform.getKoin().get<IosBannerAdFactory>() }.getOrNull()
    }
    UIKitView(
        factory = { factory?.createBannerAdView(AdIds.bannerIdIos) ?: UIView() },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true,
        ),
    )
}
