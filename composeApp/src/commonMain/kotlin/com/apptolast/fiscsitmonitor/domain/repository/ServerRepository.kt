package com.apptolast.fiscsitmonitor.domain.repository

import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import kotlinx.coroutines.flow.StateFlow

interface ServerRepository {
    val server: StateFlow<ServerDto?>
    val metrics: StateFlow<ServerMetricsDto?>
    val players: StateFlow<List<PlayerDto>>
    val powerCircuits: StateFlow<List<PowerCircuitDto>>
    val productionItems: StateFlow<List<ProductionItemDto>>

    suspend fun loadInitialData()
}
