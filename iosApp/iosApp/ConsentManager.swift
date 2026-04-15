import Foundation
import GoogleMobileAds
import UserMessagingPlatform

/// Handles the Google UMP (User Messaging Platform) consent flow on iOS and defers
/// `MobileAds.shared.start()` until `canRequestAds == true`, in compliance with GDPR/EEA
/// requirements for AdMob publishers (mandatory since Jan 2024).
///
/// Flow (per https://developers.google.com/admob/ios/privacy):
///   1. (DEBUG only) `ConsentInformation.shared.reset()` — clears cached consent state so
///      the form re-appears on every launch. Never call this in production.
///   2. Build `RequestParameters` with `.EEA` debug geography in DEBUG so the form simulates
///      an EEA device regardless of real IP. Simulators are test devices by default, so no
///      `testDeviceIdentifiers` is needed for them. On a physical device you must copy the
///      hashed ID printed to the Xcode console on first run and add it to `DebugSettings`.
///   3. `requestConsentInfoUpdate(with:)` — async, resolves user region + consent status.
///   4. `ConsentForm.loadAndPresentIfRequired(from: nil)` — presents the IAB TCF 2.2 form
///      if required. `nil` means "let the SDK find the key window" (SwiftUI-friendly).
///   5. When the flow completes, if `canRequestAds` is true, start Mobile Ads. Otherwise
///      leave the SDK stopped — banners will render empty.
///
/// **Precondition outside code**: the publisher (you) must create and **publish** a GDPR
/// message in AdMob Console → Privacy & messaging → GDPR for this app's AdMob App ID.
/// Without a published message, UMP has nothing to present and `formStatus` will resolve to
/// `.unavailable` even with the debug geography forced to EEA.
///
/// Triggered from `ContentView.task` so it runs on main actor, after the window is attached
/// (required for the form to present), and only once per app launch.
@MainActor
final class ConsentManager {
    static let shared = ConsentManager()
    private(set) var isReady = false
    private var mobileAdsStarted = false

    private init() {
    }

    /// Runs the consent flow and, if allowed, starts the Mobile Ads SDK. Idempotent — safe
    /// to call multiple times; only starts MobileAds once.
    func gatherConsentAndStart() async {
        #if DEBUG
        // Clear cached consent state so the form reliably re-appears on every launch during
        // development. Google flags this as "testing purposes only — do not ship to prod".
        print("[ConsentManager] DEBUG build → resetting cached consent state.")
        ConsentInformation.shared.reset()
        #endif

        let parameters = RequestParameters()
        #if DEBUG
        let debug = DebugSettings()
        // Activa esto solo si quieres forzar la aparición del mensaje de GDPR para pruebas
        // debug.geography = .EEA 
        parameters.debugSettings = debug
        #endif

        print("[ConsentManager] requestConsentInfoUpdate …")
        await withCheckedContinuation { (continuation: CheckedContinuation<Void, Never>) in
            ConsentInformation.shared.requestConsentInfoUpdate(with: parameters) { error in
                if let error = error {
                    print("[ConsentManager] info update error: \(error.localizedDescription)")
                }
                // Continuamos incluso si hay error para no bloquear la app
                continuation.resume()
            }
        }

        do {
            try await ConsentForm.loadAndPresentIfRequired(from: nil)
            print(
                "[ConsentManager] form flow completed. " +
                    "canRequestAds=\(ConsentInformation.shared.canRequestAds)"
            )
        } catch {
            print("[ConsentManager] form error: \(error.localizedDescription)")
        }

        startAdsIfAllowed()
    }

    private func startAdsIfAllowed() {
        guard ConsentInformation.shared.canRequestAds else {
            print("[ConsentManager] canRequestAds=false → skipping MobileAds.shared.start()")
            return
        }
        guard !mobileAdsStarted else {
            return
        }
        mobileAdsStarted = true
        print("[ConsentManager] Starting MobileAds SDK.")
        MobileAds.shared.start { [weak self] _ in
            DispatchQueue.main.async {
                self?.isReady = true
                // Notify the rest of the app that ads can now be requested
                NotificationCenter.default.post(name: .adsAllowed, object: nil)
            }
        }
    }
}

extension Notification.Name {
    static let adsAllowed = Notification.Name("AdsAllowed")
}

private extension ConsentStatus {
    var debugLabel: String {
        switch self {
        case .unknown: return "unknown"
        case .required: return "required"
        case .notRequired: return "notRequired"
        case .obtained: return "obtained"
        @unknown default: return "unknown(\(rawValue))"
        }
    }
}

private extension FormStatus {
    var debugLabel: String {
        switch self {
        case .unknown: return "unknown"
        case .available: return "available"
        case .unavailable: return "unavailable"
        @unknown default: return "unknown(\(rawValue))"
        }
    }
}
