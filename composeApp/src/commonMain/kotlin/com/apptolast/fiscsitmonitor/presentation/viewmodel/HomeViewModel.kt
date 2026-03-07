package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.apptolast.fiscsitmonitor.data.remote.websocket.WebSocketEventDispatcher
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import com.apptolast.fiscsitmonitor.domain.repository.FactoryRepository
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val serverRepository: ServerRepository,
    private val energyRepository: EnergyRepository,
    private val factoryRepository: FactoryRepository,
    private val logisticsRepository: LogisticsRepository,
    private val webSocketClient: ReverbWebSocketClient,
    private val webSocketEventDispatcher: WebSocketEventDispatcher,
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        webSocketEventDispatcher.start()
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                serverRepository.loadInitialData()
                val serverId = serverRepository.server.value?.id
                if (serverId != null) {
                    webSocketClient.connect(serverId)
                    startFallbackPolling()
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                serverRepository.refreshMetrics()
                serverRepository.refreshPlayers()
                serverRepository.refreshPower()
                serverRepository.refreshProduction()
            } catch (_: Exception) { }
        }
    }

    private fun startFallbackPolling() {
        viewModelScope.launch {
            while (true) {
                delay(30_000L)
                if (webSocketClient.connectionState.value != ConnectionState.CONNECTED) {
                    try {
                        refresh()
                    } catch (_: Exception) { }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect()
    }
}
