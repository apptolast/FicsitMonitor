package com.apptolast.fiscsitmonitor.presentation.ui.screens.logistics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LogisticsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LogisticsScreen(
    viewModel: LogisticsViewModel = koinViewModel(),
) {
    val trains by viewModel.trains.collectAsStateWithLifecycle()
    val drones by viewModel.drones.collectAsStateWithLifecycle()
    val players by viewModel.players.collectAsStateWithLifecycle()

    LogisticsContent(
        trains = trains,
        drones = drones,
        players = players,
    )
}
