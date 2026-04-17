package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKUIDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.WKWindowFeatures
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(url: String, navigator: WebViewNavigator, modifier: Modifier) {
    // WKWebView.navigationDelegate and UIDelegate are weak — hold the delegate strongly so
    // ARC doesn't collect it while the composable is alive.
    var retainedDelegate by remember { mutableStateOf<WebViewDelegate?>(null) }

    UIKitView(
        factory = {
            WKWebView().apply {
                NSURL.URLWithString(url)?.let { nsUrl ->
                    loadRequest(NSURLRequest(nsUrl))
                }
            }
        },
        update = { webView ->
            if (retainedDelegate == null) {
                val d = WebViewDelegate(webView, navigator)
                retainedDelegate = d
                webView.navigationDelegate = d
                webView.UIDelegate = d
                navigator.goBackAction = { webView.goBack() }
            }
        },
        modifier = modifier,
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true,
        ),
    )
}

/**
 * Combined WKNavigationDelegate + WKUIDelegate:
 * - Tracks canGoBack after each page load and updates [WebViewNavigator].
 * - Redirects target="_blank" links into the same WebView instead of discarding them.
 */
private class WebViewDelegate(
    private val host: WKWebView,
    private val navigator: WebViewNavigator,
) : NSObject(), WKNavigationDelegateProtocol, WKUIDelegateProtocol {

    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        navigator.canGoBack = webView.canGoBack()
    }

    override fun webView(
        webView: WKWebView,
        createWebViewWithConfiguration: WKWebViewConfiguration,
        forNavigationAction: WKNavigationAction,
        windowFeatures: WKWindowFeatures,
    ): WKWebView? {
        if (forNavigationAction.targetFrame == null) {
            host.loadRequest(forNavigationAction.request)
        }
        return null
    }
}
