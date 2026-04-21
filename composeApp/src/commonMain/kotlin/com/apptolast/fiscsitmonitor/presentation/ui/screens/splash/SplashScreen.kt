package com.apptolast.fiscsitmonitor.presentation.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SplashDestination
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SplashViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.app_title
import ficsitmonitor.composeapp.generated.resources.splash_subtitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    onDestinationResolved: (SplashDestination) -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        if (destination != SplashDestination.Pending) {
            onDestinationResolved(destination)
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.app_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(Res.string.splash_subtitle),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview
@Composable
private fun PreviewSplashScreen() {
    FicsitMonitorTheme {
        SplashContent()
    }
}
