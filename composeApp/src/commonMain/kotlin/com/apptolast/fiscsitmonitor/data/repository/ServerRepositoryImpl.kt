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
        _server.value = servers.firstOrNull()
    }

    private fun deduplicateCircuits(circuits: List<PowerCircuitDto>): List<PowerCircuitDto> {
        return circuits.associateBy { it.circuitGroupId }.values.toList()
    }

    fun updateServer(server: ServerDto) { _server.value = server }
    fun updateMetrics(metrics: ServerMetricsDto) { _metrics.value = metrics }
    fun updatePlayers(players: List<PlayerDto>) { _players.value = players }
    fun updatePowerCircuits(circuits: List<PowerCircuitDto>) {
        _powerCircuits.value = deduplicateCircuits(circuits)
    }
    fun updateProductionItems(items: List<ProductionItemDto>) { _productionItems.value = items }
}
