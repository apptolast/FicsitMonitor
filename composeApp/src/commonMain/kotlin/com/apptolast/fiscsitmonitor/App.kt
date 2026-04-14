package com.apptolast.fiscsitmonitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.apptolast.fiscsitmonitor.presentation.navigation.FicsitNavHost
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme

@Composable
fun App() {
    FicsitMonitorTheme {
        val navController = rememberNavController()
        FicsitNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        )
    }
}
