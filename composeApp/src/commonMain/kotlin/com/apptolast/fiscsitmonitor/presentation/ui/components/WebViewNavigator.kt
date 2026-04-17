package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class WebViewNavigator {
    var canGoBack by mutableStateOf(false)
        internal set

    internal var goBackAction: (() -> Unit)? = null

    fun goBack() = goBackAction?.invoke()
}

@Composable
fun rememberWebViewNavigator(): WebViewNavigator = remember { WebViewNavigator() }
