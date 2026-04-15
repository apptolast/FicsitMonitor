package com.apptolast.fiscsitmonitor.data.server.dto

import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ResourceSinkDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardSnapshotDto(
    val server: ServerDto? = null,
    val metrics: ServerMetricsDto? = null,
    val players: List<PlayerDto> = emptyList(),
    val circuits: List<PowerCircuitDto> = emptyList(),
    val production: List<ProductionItemDto> = emptyList(),
    val trains: List<TrainDto> = emptyList(),
    val drones: List<DroneStationDto> = emptyList(),
    val generators: List<GeneratorDto> = emptyList(),
    val extractors: List<ExtractorDto> = emptyList(),
    @SerialName("world_inventory") val worldInventory: List<WorldInventoryItemDto> = emptyList(),
    @SerialName("resource_sink") val resourceSink: ResourceSinkDto? = null,
    val buildings: List<FactoryBuildingDto> = emptyList(),
)
