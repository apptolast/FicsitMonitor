package com.apptolast.fiscsitmonitor.data.remote.websocket

import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ResourceSinkDto
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray

sealed class LiveWsEvent {
    data class ServerStatus(val status: String) : LiveWsEvent()
    object MetricsUpdated : LiveWsEvent()
    object PowerUpdated : LiveWsEvent()
    data class FuseTriggered(val circuits: Int) : LiveWsEvent()
    object ProductionUpdated : LiveWsEvent()
    data class PlayersOnline(val online: Int) : LiveWsEvent()
    object TrainsUpdated : LiveWsEvent()
    data class TrainsDerailed(val count: Int) : LiveWsEvent()
    object DronesUpdated : LiveWsEvent()
    object GeneratorsUpdated : LiveWsEvent()
    object FactoryUpdated : LiveWsEvent()
    object ExtractorsUpdated : LiveWsEvent()
    object InventoryUpdated : LiveWsEvent()
    object SinkUpdated : LiveWsEvent()
}

class WebSocketEventDispatcher(
    private val webSocketClient: ReverbWebSocketClient,
    private val serverRepository: ServerRepositoryImpl,
    private val energyRepository: EnergyRepositoryImpl,
    private val factoryRepository: FactoryRepositoryImpl,
    private val logisticsRepository: LogisticsRepositoryImpl,
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
    private val tag = "WS_DISPATCHER"

    private val _liveEvents = MutableSharedFlow<LiveWsEvent>(extraBufferCapacity = 64)
    val liveEvents: SharedFlow<LiveWsEvent> = _liveEvents.asSharedFlow()

    fun start() {
        println("[$tag] Starting event dispatcher")
        scope.launch {
            webSocketClient.events.collect { event ->
                try {
                    println("[$tag] Received event: ${event.event} | data keys: ${event.data.keys}")
                    dispatch(event)
                } catch (e: Exception) {
                    println("[$tag] ERROR dispatching '${event.event}': ${e.message}")
                    println("[$tag] Raw data: ${event.data}")
                }
            }
        }
    }

    private fun dispatch(event: WebSocketEvent) {
        val liveEvent: LiveWsEvent? = when (event.event) {
            "server.status.updated" -> {
                handleServerStatus(event.data)
                val status = event.data["status"]?.toString()?.trim('"') ?: "unknown"
                LiveWsEvent.ServerStatus(status)
            }

            "server.metrics.updated" -> {
                handleServerMetrics(event.data)
                LiveWsEvent.MetricsUpdated
            }

            "power.updated" -> {
                handlePowerUpdated(event.data)
                val fused = serverRepository.powerCircuits.value.count { it.fuseTriggered }
                if (fused > 0) LiveWsEvent.FuseTriggered(fused)
                else LiveWsEvent.PowerUpdated
            }

            "production.updated" -> {
                handleProductionUpdated(event.data)
                LiveWsEvent.ProductionUpdated
            }

            "players.updated" -> {
                handlePlayersUpdated(event.data)
                val online = serverRepository.players.value.count { it.isOnline }
                LiveWsEvent.PlayersOnline(online)
            }

            "trains.updated" -> {
                handleTrainsUpdated(event.data)
                val derailed = logisticsRepository.trains.value.count { it.isDerailed }
                if (derailed > 0) LiveWsEvent.TrainsDerailed(derailed)
                else LiveWsEvent.TrainsUpdated
            }

            "drones.updated" -> {
                handleDronesUpdated(event.data)
                LiveWsEvent.DronesUpdated
            }

            "generators.updated" -> {
                handleGeneratorsUpdated(event.data)
                LiveWsEvent.GeneratorsUpdated
            }

            "factory.updated" -> {
                handleFactoryUpdated(event.data)
                LiveWsEvent.FactoryUpdated
            }

            "extractors.updated" -> {
                handleExtractorsUpdated(event.data)
                LiveWsEvent.ExtractorsUpdated
            }

            "world_inventory.updated" -> {
                handleWorldInventoryUpdated(event.data)
                LiveWsEvent.InventoryUpdated
            }

            "resource_sink.updated" -> {
                handleResourceSinkUpdated(event.data)
                LiveWsEvent.SinkUpdated
            }

            else -> null
        }
        liveEvent?.let { _liveEvents.tryEmit(it) }
    }

    private fun handleServerStatus(data: JsonObject) {
        val currentServer = serverRepository.server.value ?: return
        val status = data["status"]?.toString()?.trim('"') ?: return
        println("[$tag] server.status -> $status")
        serverRepository.updateServer(currentServer.copy(status = status))
    }

    private fun handleServerMetrics(data: JsonObject) {
        val metrics = json.decodeFromJsonElement<ServerMetricsDto>(data)
        println("[$tag] server.metrics -> tickRate=${metrics.tickRate}, players=${metrics.playerCount}, tier=${metrics.techTier}, phase=${metrics.gamePhase}")
        serverRepository.updateMetrics(metrics)
    }

    private fun handlePowerUpdated(data: JsonObject) {
        val circuitsElement = data["circuits"]
            ?: run { println("[$tag] power.updated -> key 'circuits' NOT FOUND in: ${data.keys}"); return }
        val circuits = json.decodeFromJsonElement<List<PowerCircuitDto>>(circuitsElement.jsonArray)
        println("[$tag] power.updated -> ${circuits.size} circuits | fuses=${circuits.count { it.fuseTriggered }}")
        serverRepository.updatePowerCircuits(circuits)
    }

    private fun handleProductionUpdated(data: JsonObject) {
        val itemsElement = data["items"]
            ?: run { println("[$tag] production.updated -> key 'items' NOT FOUND in: ${data.keys}"); return }
        val items = json.decodeFromJsonElement<List<ProductionItemDto>>(itemsElement.jsonArray)
        println("[$tag] production.updated -> ${items.size} items | producing=${items.count { it.currentProd > 0 }}")
        serverRepository.updateProductionItems(items)
    }

    private fun handlePlayersUpdated(data: JsonObject) {
        val playersElement = data["players"]
            ?: run { println("[$tag] players.updated -> key 'players' NOT FOUND in: ${data.keys}"); return }
        val players = json.decodeFromJsonElement<List<PlayerDto>>(playersElement.jsonArray)
        println("[$tag] players.updated -> ${players.size} players | online=${players.count { it.isOnline }}")
        serverRepository.updatePlayers(players)
    }

    private fun handleTrainsUpdated(data: JsonObject) {
        val trainsElement = data["trains"]
            ?: run { println("[$tag] trains.updated -> key 'trains' NOT FOUND in: ${data.keys}"); return }
        val trains = json.decodeFromJsonElement<List<TrainDto>>(trainsElement.jsonArray)
        println("[$tag] trains.updated -> ${trains.size} trains | derailed=${trains.count { it.isDerailed }}")
        logisticsRepository.updateTrains(trains)
    }

    private fun handleDronesUpdated(data: JsonObject) {
        val stationsElement = data["stations"]
            ?: run { println("[$tag] drones.updated -> key 'stations' NOT FOUND in: ${data.keys}"); return }
        val drones = json.decodeFromJsonElement<List<DroneStationDto>>(stationsElement.jsonArray)
        println("[$tag] drones.updated -> ${drones.size} stations")
        logisticsRepository.updateDrones(drones)
    }

    private fun handleGeneratorsUpdated(data: JsonObject) {
        val generatorsElement = data["generators"]
            ?: run { println("[$tag] generators.updated -> key 'generators' NOT FOUND in: ${data.keys}"); return }
        val generators = json.decodeFromJsonElement<List<GeneratorDto>>(generatorsElement.jsonArray)
        println("[$tag] generators.updated -> ${generators.size} generators | totalMW=${generators.sumOf { it.capacityMw }}")
        energyRepository.updateGenerators(generators)
    }

    private fun handleFactoryUpdated(data: JsonObject) {
        val buildingsElement = data["buildings"]
            ?: run { println("[$tag] factory.updated -> key 'buildings' NOT FOUND in: ${data.keys}"); return }
        val buildings = json.decodeFromJsonElement<List<FactoryBuildingDto>>(buildingsElement.jsonArray)
        println("[$tag] factory.updated -> ${buildings.size} buildings | producing=${buildings.count { it.isProducing }}")
        factoryRepository.updateBuildings(buildings)
    }

    private fun handleExtractorsUpdated(data: JsonObject) {
        val extractorsElement = data["extractors"]
            ?: run { println("[$tag] extractors.updated -> key 'extractors' NOT FOUND in: ${data.keys}"); return }
        val extractors = json.decodeFromJsonElement<List<ExtractorDto>>(extractorsElement.jsonArray)
        println("[$tag] extractors.updated -> ${extractors.size} extractors | producing=${extractors.count { it.isProducing }}")
        factoryRepository.updateExtractors(extractors)
    }

    private fun handleWorldInventoryUpdated(data: JsonObject) {
        val inventoryElement = data["inventory"]
            ?: run { println("[$tag] world_inventory.updated -> key 'inventory' NOT FOUND in: ${data.keys}"); return }
        val items = json.decodeFromJsonElement<List<WorldInventoryItemDto>>(inventoryElement.jsonArray)
        println("[$tag] world_inventory.updated -> ${items.size} items")
        factoryRepository.updateWorldInventory(items)
    }

    private fun handleResourceSinkUpdated(data: JsonObject) {
        val sinkElement = data["sink"]
            ?: run { println("[$tag] resource_sink.updated -> key 'sink' NOT FOUND in: ${data.keys}"); return }
        val sink = json.decodeFromJsonElement<ResourceSinkDto>(sinkElement)
        println("[$tag] resource_sink.updated -> points=${sink.totalPoints}, coupons=${sink.numCoupon}")
        factoryRepository.updateResourceSink(sink)
    }
}
