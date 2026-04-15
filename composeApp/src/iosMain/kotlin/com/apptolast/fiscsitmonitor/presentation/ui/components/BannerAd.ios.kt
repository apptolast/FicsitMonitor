package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import com.apptolast.fiscsitmonitor.ads.AdIds
import com.apptolast.fiscsitmonitor.ads.IosBannerAdFactory
import org.koin.core.component.get
import org.koin.mp.KoinPlatform
import platform.UIKit.UIView

@Composable
actual fun BannerAd(modifier: Modifier) {
    // If the Swift factory hasn't been registered yet (e.g. running an old iosApp without
    // GoogleMobileAds SDK wired up), fall back to an empty UIView so the layout doesn't break.
    val factory: IosBannerAdFactory? = remember {
        runCatching { KoinPlatform.getKoin().get<IosBannerAdFactory>() }.getOrNull()
    }
    UIKitView(
        factory = {
            factory?.create(AdIds.bannerIdIos) ?: UIView()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
    )
}
