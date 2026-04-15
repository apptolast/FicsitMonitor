package com.apptolast.fiscsitmonitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apptolast.fiscsitmonitor.presentation.navigation.FicsitNavHost
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme

@Composable
fun App() {
    FicsitMonitorTheme {
        FicsitNavHost(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        )
    }
}
