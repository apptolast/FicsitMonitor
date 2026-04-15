package com.apptolast.fiscsitmonitor.ads

import platform.UIKit.UIView

/**
 * Kotlin-side view of the Swift factory that builds and loads Native Advanced Ad views.
 *
 * This interface is exported from the ComposeApp framework as an Objective-C protocol.
 * `iosApp/iosApp/AdMobNativeAdFactory.swift` implements it using [NativeAdView] from the
 * Google Mobile Ads SDK v12+ and registers the instance with Koin at startup via
 * [KoinInitializerKt.registerNativeAdFactory].
 *
 * The factory creates a UIView container synchronously and starts the async [AdLoader] load
 * inside Swift. When the ad loads, Swift populates the container with the fully themed
 * [NativeAdView]. The Kotlin [NativeAd] composable wraps this UIView in UIKitView.
 */
interface IosNativeAdFactory {
    fun createNativeAdView(adUnitId: String): UIView
}
