package com.apptolast.fiscsitmonitor.presentation.ui.screens.webviewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.presentation.ui.components.BannerAd
import com.apptolast.fiscsitmonitor.presentation.ui.components.PlatformWebView
import com.apptolast.fiscsitmonitor.presentation.ui.components.rememberWebViewNavigator

@Composable
fun WebViewerScreen(url: String) {
    val navigator = rememberWebViewNavigator()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            PlatformWebView(
                url = url,
                navigator = navigator,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
            BannerAd(modifier = Modifier.fillMaxWidth())
        }

        // Floating back button: appears when the WebView has history to navigate back through.
        // On Android this is redundant (BackHandler intercepts the system gesture), but on iOS
        // Compose's own edge-swipe always wins over WKWebView's gesture recognizer, so this
        // button is the primary way to navigate back within the web on iOS.
        AnimatedVisibility(
            visible = navigator.canGoBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(8.dp),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            SmallFloatingActionButton(
                onClick = { navigator.goBack() },
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = null,
                )
            }
        }
    }
}
