package com.apptolast.fiscsitmonitor

import com.apptolast.fiscsitmonitor.ads.IosBannerAdFactory
import com.apptolast.fiscsitmonitor.ads.IosNativeAdFactory
import com.apptolast.fiscsitmonitor.di.initKoin
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun doInitKoin() {
    initKoin()
}

/**
 * Called from Swift after `doInitKoin()` to register the Google Mobile Ads Native Ad factory
 * implemented in Swift. Kept separate so `doInitKoin()` stays backward-compatible.
 */
fun registerNativeAdFactory(factory: IosNativeAdFactory) {
    loadKoinModules(
        module {
            single<IosNativeAdFactory> { factory }
        },
    )
}

/**
 * Called from Swift after `doInitKoin()` to register the Google Mobile Ads Banner Ad factory
 * implemented in Swift.
 */
fun registerBannerAdFactory(factory: IosBannerAdFactory) {
    loadKoinModules(
        module {
            single<IosBannerAdFactory> { factory }
        },
    )
}
