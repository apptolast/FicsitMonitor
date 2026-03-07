package com.apptolast.fiscsitmonitor.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val server by viewModel.server.collectAsStateWithLifecycle()
    val metrics by viewModel.metrics.collectAsStateWithLifecycle()
    val players by viewModel.players.collectAsStateWithLifecycle()
    val circuits by viewModel.powerCircuits.collectAsStateWithLifecycle()
    val production by viewModel.productionItems.collectAsStateWithLifecycle()
    val extractors by viewModel.extractors.collectAsStateWithLifecycle()
    val generators by viewModel.generators.collectAsStateWithLifecycle()
    val trains by viewModel.trains.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading && server == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            error != null && server == null -> {
                Text(
                    text = error ?: "Error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            else -> {
                HomeContent(
                    server = server,
                    metrics = metrics,
                    players = players,
                    circuits = circuits,
                    production = production,
                    extractors = extractors,
                    generators = generators,
                    trains = trains,
                )
            }
        }
    }
}
