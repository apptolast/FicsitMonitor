import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Note: MobileAds is intentionally NOT started here. It is gated on the UMP (GDPR)
        // consent flow and triggered from ContentView.task via ConsentManager, so no ad
        // server calls happen before the user has consented.
        KoinInitializerKt.doInitKoin()

        // Register the Swift-side factory that builds NativeAdView instances; the Kotlin
        // `actual fun NativeAd()` looks this up from Koin at compose time.
        KoinInitializerKt.registerNativeAdFactory(factory: AdMobNativeAdFactory())

        // Register the Swift-side factory that builds BannerView instances; the Kotlin
        // `actual fun BannerAd()` looks this up from Koin at compose time.
        KoinInitializerKt.registerBannerAdFactory(factory: AdMobBannerAdFactory())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
