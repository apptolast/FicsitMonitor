package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.bootstrap.ServerBootstrapper
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.data.remote.websocket.ConnectionState
import com.apptolast.fiscsitmonitor.data.remote.websocket.ReverbWebSocketClient
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import com.apptolast.fiscsitmonitor.domain.repository.FactoryRepository
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    serverRepository: ServerRepository,
    energyRepository: EnergyRepository,
    factoryRepository: FactoryRepository,
    logisticsRepository: LogisticsRepository,
    webSocketClient: ReverbWebSocketClient,
    private val bootstrapper: ServerBootstrapper,
) : ViewModel() {

    val server: StateFlow<ServerDto?> = serverRepository.server
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val metrics: StateFlow<ServerMetricsDto?> = serverRepository.metrics
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val players: StateFlow<List<PlayerDto>> = serverRepository.players
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val powerCircuits: StateFlow<List<PowerCircuitDto>> = serverRepository.powerCircuits
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val productionItems: StateFlow<List<ProductionItemDto>> = serverRepository.productionItems
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val extractors: StateFlow<List<ExtractorDto>> = factoryRepository.extractors
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val generators: StateFlow<List<GeneratorDto>> = energyRepository.generators
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val trains: StateFlow<List<TrainDto>> = logisticsRepository.trains
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val connectionState: StateFlow<ConnectionState> = webSocketClient.connectionState

    val isLoading: StateFlow<Boolean> = bootstrapper.isSnapshotLoading
    val error: StateFlow<String?> = bootstrapper.lastError

    fun refresh() {
        viewModelScope.launch { bootstrapper.refreshSnapshot() }
    }
}
