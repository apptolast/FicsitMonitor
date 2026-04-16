package com.apptolast.fiscsitmonitor.presentation.ui.components

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PlatformWebView(url: String, navigator: WebViewNavigator, modifier: Modifier) {
    BackHandler(enabled = navigator.canGoBack) {
        navigator.goBack()
    }

    AndroidView(
        factory = { context ->
            android.webkit.WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : android.webkit.WebViewClient() {
                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        navigator.canGoBack = view?.canGoBack() == true
                    }
                }
                loadUrl(url)
            }.also { webView ->
                navigator.goBackAction = { webView.goBack() }
            }
        },
        modifier = modifier,
    )
}
