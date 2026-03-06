package com.apptolast.fiscsitmonitor.data.remote.websocket

import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import com.apptolast.fiscsitmonitor.data.repository.EnergyRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.FactoryRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.LogisticsRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.ServerRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray

class WebSocketEventDispatcher(
    private val webSocketClient: ReverbWebSocketClient,
    private val serverRepository: ServerRepositoryImpl,
    private val energyRepository: EnergyRepositoryImpl,
    private val factoryRepository: FactoryRepositoryImpl,
    private val logisticsRepository: LogisticsRepositoryImpl,
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true }

    fun start() {
        scope.launch {
            webSocketClient.events.collect { event ->
                try {
                    dispatch(event)
                } catch (_: Exception) { }
            }
        }
    }

    private fun dispatch(event: WebSocketEvent) {
        when (event.event) {
            "server.status.updated" -> handleServerStatus(event.data)
            "server.metrics.updated" -> handleServerMetrics(event.data)
            "power.updated" -> handlePowerUpdated(event.data)
            "production.updated" -> handleProductionUpdated(event.data)
            "players.updated" -> handlePlayersUpdated(event.data)
            "trains.updated" -> handleTrainsUpdated(event.data)
            "drones.updated" -> handleDronesUpdated(event.data)
            "generators.updated" -> handleGeneratorsUpdated(event.data)
            "factory.status.updated" -> handleFactoryUpdated(event.data)
            "extractors.updated" -> handleExtractorsUpdated(event.data)
            "world.inventory.updated" -> handleWorldInventoryUpdated(event.data)
        }
    }

    private fun handleServerStatus(data: JsonObject) {
        val currentServer = serverRepository.server.value ?: return
        val status = data["status"]?.toString()?.trim('"') ?: return
        serverRepository.updateServer(currentServer.copy(status = status))
    }

    private fun handleServerMetrics(data: JsonObject) {
        val metrics = json.decodeFromJsonElement<ServerMetricsDto>(data)
        serverRepository.updateMetrics(metrics)
    }

    private fun handlePowerUpdated(data: JsonObject) {
        val circuitsElement = data["circuits"] ?: return
        val circuits = json.decodeFromJsonElement<List<PowerCircuitDto>>(circuitsElement.jsonArray)
        serverRepository.updatePowerCircuits(circuits)
    }

    private fun handleProductionUpdated(data: JsonObject) {
        val itemsElement = data["production"] ?: data["items"] ?: return
        val items = json.decodeFromJsonElement<List<ProductionItemDto>>(itemsElement.jsonArray)
        serverRepository.updateProductionItems(items)
    }

    private fun handlePlayersUpdated(data: JsonObject) {
        val playersElement = data["players"] ?: return
        val players = json.decodeFromJsonElement<List<PlayerDto>>(playersElement.jsonArray)
        serverRepository.updatePlayers(players)
    }

    private fun handleTrainsUpdated(data: JsonObject) {
        val trainsElement = data["trains"] ?: return
        val trains = json.decodeFromJsonElement<List<TrainDto>>(trainsElement.jsonArray)
        logisticsRepository.updateTrains(trains)
    }

    private fun handleDronesUpdated(data: JsonObject) {
        val dronesElement = data["drones"] ?: return
        val drones = json.decodeFromJsonElement<List<DroneStationDto>>(dronesElement.jsonArray)
        logisticsRepository.updateDrones(drones)
    }

    private fun handleGeneratorsUpdated(data: JsonObject) {
        val generatorsElement = data["generators"] ?: return
        val generators = json.decodeFromJsonElement<List<GeneratorDto>>(generatorsElement.jsonArray)
        energyRepository.updateGenerators(generators)
    }

    private fun handleFactoryUpdated(data: JsonObject) {
        val buildingsElement = data["buildings"] ?: data["factory"] ?: return
        val buildings = json.decodeFromJsonElement<List<FactoryBuildingDto>>(buildingsElement.jsonArray)
        factoryRepository.updateBuildings(buildings)
    }

    private fun handleExtractorsUpdated(data: JsonObject) {
        val extractorsElement = data["extractors"] ?: return
        val extractors = json.decodeFromJsonElement<List<ExtractorDto>>(extractorsElement.jsonArray)
        factoryRepository.updateExtractors(extractors)
    }

    private fun handleWorldInventoryUpdated(data: JsonObject) {
        val inventoryElement = data["items"] ?: data["inventory"] ?: return
        val items = json.decodeFromJsonElement<List<WorldInventoryItemDto>>(inventoryElement.jsonArray)
        factoryRepository.updateWorldInventory(items)
    }
}
