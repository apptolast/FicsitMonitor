import ComposeApp
import GoogleMobileAds
import UIKit
import UserMessagingPlatform

/// Swift implementation of the Kotlin-exported `IosNativeAdFactory` protocol.
@MainActor
final class AdMobNativeAdFactory: NSObject, IosNativeAdFactory {

    /// Custom UIView subclass to hold the adUnitId, allowing for delayed loading.
    private final class AdContainerView: UIView {
        let adUnitId: String

        init(adUnitId: String) {
            self.adUnitId = adUnitId
            super.init(frame: .zero)
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
    }

    // We must retain the AdLoaders AND their delegate bridges. `AdLoader.delegate` is `weak`
    // in the Google Mobile Ads SDK, so if the bridge is a local variable it gets deallocated
    // immediately when the function returns — and when the ad response arrives, the callback
    // hits a nil delegate and the container is never populated.
    private var activeLoaders: [ObjectIdentifier: (loader: AdLoader, bridge: NativeAdLoaderBridge)] = [:]
    // Observer tokens returned by `NotificationCenter.addObserver(forName:...:using:)`. The
    // block-based API returns an opaque token that must be passed back to `removeObserver(_:)`
    // — passing `self` there silently does nothing.
    private var pendingObservers: [ObjectIdentifier: NSObjectProtocol] = [:]

    func createNativeAdView(adUnitId: String) -> UIView {
        let container = AdContainerView(adUnitId: adUnitId)
        container.backgroundColor = UIColor(hex: "#141414")
        container.layer.cornerRadius = 12
        container.layer.borderWidth = 1
        container.layer.borderColor = UIColor(hex: "#2F2F2F").cgColor
        container.clipsToBounds = true

        loadAd(into: container)
        return container
    }

    private func loadAd(into container: AdContainerView) {
        // Si ya hay un anuncio cargando o cargado para este contenedor, no hacemos nada.
        let loaderId = ObjectIdentifier(container)
        if activeLoaders[loaderId] != nil {
            return
        }

        // Si NO podemos pedir anuncios por consentimiento, esperamos.
        // Pero no dependemos de 'isReady' que es una bandera nuestra, sino de la oficial del SDK.
        guard ConsentInformation.shared.canRequestAds else {
            // Si ya hay un observer pendiente para este container, no registrar otro.
            if pendingObservers[loaderId] != nil {
                return
            }
            print("[AdMobNativeAdFactory] Consentimiento no disponible aún. Esperando...")
            let token = NotificationCenter.default.addObserver(
                forName: .adsAllowed,
                object: nil,
                queue: .main
            ) { [weak self, weak container] _ in
                guard let self = self, let container = container else {
                    return
                }
                if let token = self.pendingObservers.removeValue(forKey: loaderId) {
                    NotificationCenter.default.removeObserver(token)
                }
                self.loadAd(into: container)
            }
            pendingObservers[loaderId] = token
            return
        }

        // Intentar obtener el rootViewController. Si es nil, reintentamos en un momento.
        guard let rootVC = Self.rootViewController() else {
            print("[AdMobNativeAdFactory] rootViewController no disponible, reintentando en 0.5s...")
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) { [weak self] in
                self?.loadAd(into: container)
            }
            return
        }

        let loader = AdLoader(
            adUnitID: container.adUnitId,
            rootViewController: rootVC,
            adTypes: [.native],
            options: nil
        )

        let bridge = NativeAdLoaderBridge(container: container) { [weak self] in
            self?.activeLoaders.removeValue(forKey: loaderId)
        }

        loader.delegate = bridge
        // Crítico: guardar tanto el loader como el bridge. El delegate del AdLoader es `weak`,
        // así que si el bridge no se retiene aquí, ARC lo libera antes de que llegue la respuesta.
        activeLoaders[loaderId] = (loader, bridge)

        print("[AdMobNativeAdFactory] Solicitando anuncio nativo: \(container.adUnitId)")
        loader.load(Request())
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
        print("[AdMobNativeAdFactory] Ad received: \(nativeAd.headline ?? "<no headline>")")
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
