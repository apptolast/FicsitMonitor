import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
            .task {
                // Kick off the GDPR/UMP consent flow once the window is attached. `.task`
                // runs the async closure once per view appearance; for the root ContentView
                // that means once per app launch. When the flow completes, MobileAds is
                // started conditionally on `ConsentInformation.shared.canRequestAds`.
                await ConsentManager.shared.gatherConsentAndStart()
            }
    }
}



