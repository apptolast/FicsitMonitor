package com.apptolast.fiscsitmonitor.presentation.ui.screens.live

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LiveScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("LIVE", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
    }
}
