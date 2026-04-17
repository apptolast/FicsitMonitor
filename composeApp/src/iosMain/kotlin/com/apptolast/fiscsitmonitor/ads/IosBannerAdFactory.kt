package com.apptolast.fiscsitmonitor.ads

import platform.UIKit.UIView

/**
 * Kotlin-side view of the Swift factory that builds and loads standard Banner Ad views.
 *
 * This interface is exported from the ComposeApp framework as an Objective-C protocol.
 * `iosApp/iosApp/AdMobBannerAdFactory.swift` implements it using [BannerView] from the
 * Google Mobile Ads SDK v12+ and registers the instance with Koin at startup.
 */
interface IosBannerAdFactory {
    fun createBannerAdView(adUnitId: String): UIView
}
