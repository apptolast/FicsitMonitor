package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Platform-specific WebView. On Android, the system back gesture/button navigates through the
 * web history first (via [BackHandler]) before popping the Compose stack. On iOS, Compose's own
 * edge-swipe gesture always takes priority over WKWebView's gesture recognizer, so web history
 * navigation is exposed via [WebViewNavigator] and handled through a UI button in the host screen.
 */
@Composable
expect fun PlatformWebView(
    url: String,
    navigator: WebViewNavigator,
    modifier: Modifier = Modifier,
)
