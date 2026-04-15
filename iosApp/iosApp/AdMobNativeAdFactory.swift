import ComposeApp
import GoogleMobileAds
import UIKit
import UserMessagingPlatform

/// Swift implementation of the Kotlin-exported `IosNativeAdFactory` protocol.
final class AdMobNativeAdFactory: NSObject, IosNativeAdFactory {

    // We must retain the AdLoaders. If they are local variables, they get
    // deallocated immediately when the function returns, cancelling the ad request.
    private var activeLoaders: [ObjectIdentifier: AdLoader] = [:]

    func createNativeAdView(adUnitId: String) -> UIView {
        let container = UIView()
        container.backgroundColor = UIColor(hex: "#141414")
        container.layer.cornerRadius = 12
        container.layer.borderWidth = 1
        container.layer.borderColor = UIColor(hex: "#2F2F2F").cgColor
        container.clipsToBounds = true

        // Check if we can request ads according to the UMP consent status.
        // If the consent form is still showing or not yet accepted, we skip the request.
        guard ConsentInformation.shared.canRequestAds else {
            print("[AdMobNativeAdFactory] Skipping ad request: Consent not yet gathered or SDK not started.")
            return container
        }

        let loader = AdLoader(
            adUnitID: adUnitId,
            rootViewController: Self.rootViewController(),
            adTypes: [.native],
            options: nil
        )

        let loaderId = ObjectIdentifier(container)
        let bridge = NativeAdLoaderBridge(container: container) { [weak self] in
            self?.activeLoaders.removeValue(forKey: loaderId)
        }

        loader.delegate = bridge
        activeLoaders[loaderId] = loader
        loader.load(Request())

        return container
    }

    private static func rootViewController() -> UIViewController? {
        UIApplication.shared.connectedScenes
        .compactMap {
            $0 as? UIWindowScene
        }
        .flatMap(\.windows)
        .first {
            $0.isKeyWindow
        }?
        .rootViewController
    }
}

// MARK: - NativeAdLoaderBridge

private final class NativeAdLoaderBridge: NSObject, AdLoaderDelegate, NativeAdLoaderDelegate {
    private weak var container: UIView?
    private var onCompleted: () -> Void

    init(container: UIView, onCompleted: @escaping () -> Void) {
        self.container = container
        self.onCompleted = onCompleted
    }

    func adLoader(_ adLoader: AdLoader, didReceive nativeAd: NativeAd) {
        defer {
            onCompleted()
        }
        guard let container = container else {
            return
        }

        DispatchQueue.main.async {
            self.populate(container: container, with: nativeAd)
        }
    }

    func adLoader(_ adLoader: AdLoader, didFailToReceiveAdWithError error: Error) {
        print("[AdMobNativeAdFactory] Ad failed to load: \(error.localizedDescription)")
        onCompleted()
    }

    private func populate(container: UIView, with nativeAd: NativeAd) {
        let adView = NativeAdView()
        adView.translatesAutoresizingMaskIntoConstraints = false
        adView.backgroundColor = .clear

        // -- Attribution label --
        let attributionLabel = UILabel()
        attributionLabel.text = "Anuncio"
        attributionLabel.font = .systemFont(ofSize: 10, weight: .semibold)
        attributionLabel.textColor = UIColor(hex: "#00FF88")
        attributionLabel.translatesAutoresizingMaskIntoConstraints = false

        // -- Icon --
        let iconView = UIImageView()
        iconView.layer.cornerRadius = 8
        iconView.clipsToBounds = true
        iconView.translatesAutoresizingMaskIntoConstraints = false
        iconView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        iconView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        if let icon = nativeAd.icon?.image {
            iconView.image = icon
            iconView.isHidden = false
        } else {
            iconView.isHidden = true
        }
        adView.iconView = iconView

        // -- Headline --
        let headlineLabel = UILabel()
        headlineLabel.text = nativeAd.headline
        headlineLabel.font = .systemFont(ofSize: 14, weight: .bold)
        headlineLabel.textColor = UIColor(hex: "#FFFFFF")
        headlineLabel.numberOfLines = 1
        headlineLabel.translatesAutoresizingMaskIntoConstraints = false
        adView.headlineView = headlineLabel

        // -- Body --
        let bodyLabel = UILabel()
        bodyLabel.text = nativeAd.body
        bodyLabel.font = .systemFont(ofSize: 12)
        bodyLabel.textColor = UIColor(hex: "#8A8A8A")
        bodyLabel.numberOfLines = 2
        bodyLabel.translatesAutoresizingMaskIntoConstraints = false
        bodyLabel.isHidden = nativeAd.body == nil
        adView.bodyView = bodyLabel

        // -- CTA button --
        var config = UIButton.Configuration.filled()
        config.title = nativeAd.callToAction
        config.baseBackgroundColor = UIColor(hex: "#00FF88")
        config.baseForegroundColor = UIColor(hex: "#0C0C0C")
        config.contentInsets = NSDirectionalEdgeInsets(top: 6, leading: 12, bottom: 6, trailing: 12)
        config.background.cornerRadius = 6

        let ctaButton = UIButton(configuration: config)
        ctaButton.translatesAutoresizingMaskIntoConstraints = false
        ctaButton.isHidden = nativeAd.callToAction == nil
        adView.callToActionView = ctaButton

        // -- Layout assembly --
        let textStack = UIStackView(arrangedSubviews: [headlineLabel, bodyLabel])
        textStack.axis = .vertical
        textStack.spacing = 2
        textStack.translatesAutoresizingMaskIntoConstraints = false

        let contentStack = UIStackView(arrangedSubviews: [iconView, textStack, ctaButton])
        contentStack.axis = .horizontal
        contentStack.alignment = .center
        contentStack.spacing = 12
        contentStack.translatesAutoresizingMaskIntoConstraints = false

        let rootStack = UIStackView(arrangedSubviews: [attributionLabel, contentStack])
        rootStack.axis = .vertical
        rootStack.spacing = 8
        rootStack.translatesAutoresizingMaskIntoConstraints = false

        adView.addSubview(rootStack)
        NSLayoutConstraint.activate([
                                        rootStack.topAnchor.constraint(equalTo: adView.topAnchor, constant: 12),
                                        rootStack.leadingAnchor.constraint(equalTo: adView.leadingAnchor, constant: 12),
                                        rootStack.trailingAnchor.constraint(equalTo: adView.trailingAnchor, constant: -12),
                                        rootStack.bottomAnchor.constraint(equalTo: adView.bottomAnchor, constant: -12),
                                    ])

        // Bind ad — must be last.
        adView.nativeAd = nativeAd

        container.addSubview(adView)
        NSLayoutConstraint.activate([
                                        adView.topAnchor.constraint(equalTo: container.topAnchor),
                                        adView.leadingAnchor.constraint(equalTo: container.leadingAnchor),
                                        adView.trailingAnchor.constraint(equalTo: container.trailingAnchor),
                                        adView.bottomAnchor.constraint(equalTo: container.bottomAnchor),
                                    ])
    }
}

// MARK: - UIColor convenience

extension UIColor {
    fileprivate convenience init(hex: String) {
        var hex = hex.trimmingCharacters(in: .whitespaces)
        if hex.hasPrefix("#") {
            hex.removeFirst()
        }
        var value: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&value)
        let r = CGFloat((value >> 16) & 0xFF) / 255
        let g = CGFloat((value >> 8) & 0xFF) / 255
        let b = CGFloat(value & 0xFF) / 255
        self.init(red: r, green: g, blue: b, alpha: 1)
    }
}
