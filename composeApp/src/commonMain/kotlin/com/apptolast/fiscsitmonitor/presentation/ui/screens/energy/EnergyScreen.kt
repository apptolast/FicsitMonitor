package com.apptolast.fiscsitmonitor.presentation.ui.screens.energy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.viewmodel.EnergyViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EnergyScreen(
    viewModel: EnergyViewModel = koinViewModel(),
) {
    val circuits by viewModel.circuits.collectAsStateWithLifecycle()
    val generators by viewModel.generators.collectAsStateWithLifecycle()

    EnergyContent(
        circuits = circuits,
        generators = generators,
    )
}
