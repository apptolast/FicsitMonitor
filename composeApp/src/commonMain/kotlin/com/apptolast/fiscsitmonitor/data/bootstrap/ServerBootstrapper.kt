package com.apptolast.fiscsitmonitor.data.bootstrap

import com.apptolast.fiscsitmonitor.data.remote.api.DashboardApiService
import com.apptolast.fiscsitmonitor.data.remote.websocket.ReverbWebSocketClient
import com.apptolast.fiscsitmonitor.data.remote.websocket.WebSocketEventDispatcher
import com.apptolast.fiscsitmonitor.data.repository.EnergyRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.FactoryRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.LogisticsRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.ServerRepositoryImpl
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.domain.model.SessionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ServerBootstrapper(
    private val session: AuthSession,
    private val dashboardApi: DashboardApiService,
    private val webSocketClient: ReverbWebSocketClient,
    private val webSocketEventDispatcher: WebSocketEventDispatcher,
    private val serverRepository: ServerRepositoryImpl,
    private val energyRepository: EnergyRepositoryImpl,
    private val factoryRepository: FactoryRepositoryImpl,
    private val logisticsRepository: LogisticsRepositoryImpl,
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val tag = "BOOTSTRAP"

    private val _isSnapshotLoading = MutableStateFlow(false)
    val isSnapshotLoading: StateFlow<Boolean> = _isSnapshotLoading.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    private var activeServerId: Int? = null
    private var observerJob: Job? = null

    fun start() {
        if (observerJob != null) return
        webSocketEventDispatcher.start()
        observerJob = scope.launch {
            session.state
                .map { (it as? SessionState.Authenticated)?.selectedServerId }
                .distinctUntilChanged()
                .collect { serverId ->
                    handleServerIdChange(serverId)
                }
        }
    }

    private suspend fun handleServerIdChange(serverId: Int?) {
        if (serverId == null) {
            println("[$tag] serverId cleared, tearing down")
            webSocketClient.disconnect()
            serverRepository.clear()
            energyRepository.clear()
            factoryRepository.clear()
            logisticsRepository.clear()
            activeServerId = null
            return
        }
        if (serverId == activeServerId) return
        println("[$tag] Bootstrapping server $serverId")
        activeServerId = serverId
        webSocketClient.disconnect()
        serverRepository.clear()
        energyRepository.clear()
        factoryRepository.clear()
        logisticsRepository.clear()
        refreshSnapshot(serverId)
        webSocketClient.connect(serverId)
    }

    suspend fun refreshSnapshot(serverId: Int? = activeServerId) {
        val id = serverId ?: return
        _isSnapshotLoading.value = true
        _lastError.value = null
        try {
            val snapshot = dashboardApi.snapshot(id)
            serverRepository.applySnapshot(snapshot)
            energyRepository.updateGenerators(snapshot.generators)
            factoryRepository.updateBuildings(snapshot.buildings)
            factoryRepository.updateExtractors(snapshot.extractors)
            factoryRepository.updateWorldInventory(snapshot.worldInventory)
            snapshot.resourceSink?.let { factoryRepository.updateResourceSink(it) }
            logisticsRepository.updateTrains(snapshot.trains)
            logisticsRepository.updateDrones(snapshot.drones)
            println("[$tag] Snapshot applied for server $id")
        } catch (t: Throwable) {
            println("[$tag] Snapshot failed: ${t.message}")
            _lastError.value = t.message
        } finally {
            _isSnapshotLoading.value = false
        }
    }
}
