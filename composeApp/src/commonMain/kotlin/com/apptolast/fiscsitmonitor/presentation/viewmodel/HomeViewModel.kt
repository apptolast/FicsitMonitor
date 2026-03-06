package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val serverRepository: ServerRepository,
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                serverRepository.loadInitialData()
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
}
