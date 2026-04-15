package com.apptolast.fiscsitmonitor

import com.apptolast.fiscsitmonitor.ads.IosNativeAdFactory
import com.apptolast.fiscsitmonitor.di.initKoin
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun doInitKoin() {
    initKoin()
}

/**
 * Called from Swift (`iosApp/iosApp/iOSApp.swift`) after `doInitKoin()` to register the Google
 * Mobile Ads Native Ad factory implemented in Swift. Kept as a separate function so `doInitKoin()`
 * stays backward-compatible and the factory registration is opt-in.
 */
fun registerNativeAdFactory(factory: IosNativeAdFactory) {
    loadKoinModules(
        module {
            single<IosNativeAdFactory> { factory }
        },
    )
}
