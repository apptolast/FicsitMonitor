import ComposeApp
import GoogleMobileAds
import UIKit

/// Swift implementation of the Kotlin-exported `IosBannerAdFactory` protocol.
/// Creates and loads a standard 320x50 BannerView for the given ad unit ID.
@MainActor
final class AdMobBannerAdFactory: NSObject, IosBannerAdFactory {

    func createBannerAdView(adUnitId: String) -> UIView {
        let bannerView = BannerView(adSize: AdSizeBanner)
        bannerView.adUnitID = adUnitId

        if let rootVC = Self.rootViewController() {
            bannerView.rootViewController = rootVC
        }

        bannerView.load(Request())
        return bannerView
    }

    private static func rootViewController() -> UIViewController? {
        UIApplication.shared.connectedScenes
        .compactMap {
            $0 as? UIWindowScene
        }
        .flatMap(\.windows)
        .first(where: \.isKeyWindow)?
        .rootViewController
    }
}
