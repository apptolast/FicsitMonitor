package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.data.remote.api.SatisfactoryApiService
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServerRepositoryImpl(
    private val api: SatisfactoryApiService,
) : ServerRepository {

    private val _server = MutableStateFlow<ServerDto?>(null)
    override val server: StateFlow<ServerDto?> = _server.asStateFlow()

    private val _metrics = MutableStateFlow<ServerMetricsDto?>(null)
    override val metrics: StateFlow<ServerMetricsDto?> = _metrics.asStateFlow()

    private val _players = MutableStateFlow<List<PlayerDto>>(emptyList())
    override val players: StateFlow<List<PlayerDto>> = _players.asStateFlow()

    private val _powerCircuits = MutableStateFlow<List<PowerCircuitDto>>(emptyList())
    override val powerCircuits: StateFlow<List<PowerCircuitDto>> = _powerCircuits.asStateFlow()

    private val _productionItems = MutableStateFlow<List<ProductionItemDto>>(emptyList())
    override val productionItems: StateFlow<List<ProductionItemDto>> = _productionItems.asStateFlow()

    override suspend fun loadInitialData() {
        val servers = api.getServers()
        val server = servers.firstOrNull() ?: return
        _server.value = server
        refreshMetrics()
        refreshPlayers()
        refreshPower()
        refreshProduction()
    }

    override suspend fun refreshMetrics() {
        val serverId = _server.value?.id ?: return
        _metrics.value = api.getServerMetrics(serverId)
    }

    override suspend fun refreshPlayers() {
        val serverId = _server.value?.id ?: return
        _players.value = api.getPlayers(serverId)
    }

    override suspend fun refreshPower() {
        val serverId = _server.value?.id ?: return
        _powerCircuits.value = api.getPowerMetrics(serverId)
    }

    override suspend fun refreshProduction() {
        val serverId = _server.value?.id ?: return
        _productionItems.value = api.getProductionMetrics(serverId)
    }

    fun updateServer(server: ServerDto) { _server.value = server }
    fun updateMetrics(metrics: ServerMetricsDto) { _metrics.value = metrics }
    fun updatePlayers(players: List<PlayerDto>) { _players.value = players }
    fun updatePowerCircuits(circuits: List<PowerCircuitDto>) { _powerCircuits.value = circuits }
    fun updateProductionItems(items: List<ProductionItemDto>) { _productionItems.value = items }
}
