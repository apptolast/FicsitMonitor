package com.apptolast.fiscsitmonitor.ads

import platform.UIKit.UIView

/**
 * Kotlin-side view of the Swift factory that creates native Google Mobile Ads banner views.
 *
 * This interface is exported from the ComposeApp framework as an Objective-C protocol. The iOS
 * app (`iosApp/iosApp/AdMobBannerAdFactory.swift`) implements it using `BannerView` from the
 * Google Mobile Ads SDK v12+ (the v11 `GADBannerView` name was dropped) and registers the
 * instance with Koin at startup via `KoinInitializerKt.registerBannerAdFactory(factory:)`.
 *
 * Keeping GoogleMobileAds out of Kotlin/Native avoids a full cocoapods-plugin migration of the
 * iOS toolchain; only the Swift side depends on the SDK.
 */
interface IosBannerAdFactory {
    fun create(adUnitId: String): UIView
}
