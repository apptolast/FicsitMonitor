package com.apptolast.fiscsitmonitor.presentation.ui.screens.factory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.viewmodel.FactoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FactoryScreen(
    viewModel: FactoryViewModel = koinViewModel(),
) {
    val buildings by viewModel.buildings.collectAsStateWithLifecycle()
    val extractors by viewModel.extractors.collectAsStateWithLifecycle()
    val worldInventory by viewModel.worldInventory.collectAsStateWithLifecycle()
    val resourceSink by viewModel.resourceSink.collectAsStateWithLifecycle()

    FactoryContent(
        buildings = buildings,
        extractors = extractors,
        worldInventory = worldInventory,
        resourceSink = resourceSink,
    )
}
