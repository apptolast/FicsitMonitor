import UIKit
import GoogleMobileAds
import ComposeApp

/// Swift implementation of the Kotlin-exported `IosBannerAdFactory` protocol.
///
/// Creates a native `BannerView` on demand using the Google Mobile Ads SDK (v12+ dropped the
/// `GAD` prefix, so `GADBannerView` / `GADRequest` / `GADMobileAds` are now `BannerView` /
/// `Request` / `MobileAds`). The factory is registered with Koin from `iOSApp.init()` via
/// `KoinInitializerKt.registerBannerAdFactory(factory:)`, and the Kotlin `actual fun BannerAd`
/// composable retrieves it from Koin at compose time.
///
/// Requires GoogleMobileAds (>= 12.0, tested with 13.2.0) added to the Xcode project via SPM:
///   File > Add Package Dependencies… > https://github.com/googleads/swift-package-manager-google-mobile-ads.git
final class AdMobBannerAdFactory: NSObject, IosBannerAdFactory {
    func create(adUnitId: String) -> UIView {
        let banner = BannerView(adSize: AdSizeBanner)
        banner.adUnitID = adUnitId
        banner.rootViewController = Self.resolveRootViewController()
        banner.load(Request())
        return banner
    }

    private static func resolveRootViewController() -> UIViewController? {
        UIApplication.shared.connectedScenes
        .compactMap {
            $0 as? UIWindowScene
        }
        .flatMap {
            $0.windows
        }
        .first {
            $0.isKeyWindow
        }?
        .rootViewController
    }
}
