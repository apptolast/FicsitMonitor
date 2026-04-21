package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.remote.websocket.LiveWsEvent
import com.apptolast.fiscsitmonitor.data.remote.websocket.WebSocketEventDispatcher
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.TimeSource

data class LiveEvent(
    val icon: String,
    val kind: LiveWsEvent,
    val timestamp: String,
)

data class LiveSummary(
    val playersOnline: Int = 0,
    val activeCircuits: Int = 0,
    val activeTrains: Int = 0,
    val itemsInProduction: Int = 0,
    val fusesTriggered: Int = 0,
)

class LiveViewModel(
    private val serverRepository: ServerRepository,
    private val logisticsRepository: LogisticsRepository,
    private val webSocketEventDispatcher: WebSocketEventDispatcher,
) : ViewModel() {

    private val startMark = TimeSource.Monotonic.markNow()
    private val _events = MutableStateFlow<List<LiveEvent>>(emptyList())
    val events: StateFlow<List<LiveEvent>> = _events.asStateFlow()

    val summary: StateFlow<LiveSummary> = combine(
        serverRepository.players,
        serverRepository.powerCircuits,
        logisticsRepository.trains,
        serverRepository.productionItems,
    ) { players, circuits, trains, production ->
        LiveSummary(
            playersOnline = players.count { it.isOnline },
            activeCircuits = circuits.size,
            activeTrains = trains.count { it.selfDriving == true },
            itemsInProduction = production.count { it.currentProd > 0 },
            fusesTriggered = circuits.count { it.fuseTriggered },
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, LiveSummary())

    init {
        viewModelScope.launch {
            webSocketEventDispatcher.liveEvents.collect { wsEvent ->
                val icon = iconFor(wsEvent)
                val elapsed = startMark.elapsedNow()
                val totalSecs = elapsed.inWholeSeconds
                val h = (totalSecs / 3600).toString().padStart(2, '0')
                val m = ((totalSecs % 3600) / 60).toString().padStart(2, '0')
                val s = (totalSecs % 60).toString().padStart(2, '0')
                val timestamp = "+$h:$m:$s"
                addEvent(
                    LiveEvent(
                        icon = icon,
                        kind = wsEvent,
                        timestamp = timestamp,
                    )
                )
            }
        }
    }

    private fun iconFor(event: LiveWsEvent): String = when (event) {
        is LiveWsEvent.PowerUpdated, is LiveWsEvent.FuseTriggered -> "⚡"
        is LiveWsEvent.FactoryUpdated -> "🏭"
        is LiveWsEvent.TrainsUpdated, is LiveWsEvent.TrainsDerailed -> "🚂"
        is LiveWsEvent.DronesUpdated -> "✈️"
        is LiveWsEvent.PlayersOnline -> "👤"
        is LiveWsEvent.ProductionUpdated -> "⚙️"
        is LiveWsEvent.GeneratorsUpdated -> "🔥"
        is LiveWsEvent.ExtractorsUpdated -> "⛏️"
        is LiveWsEvent.ServerStatus -> "🖥️"
        is LiveWsEvent.MetricsUpdated -> "📊"
        is LiveWsEvent.InventoryUpdated -> "📦"
        is LiveWsEvent.SinkUpdated -> "⭐"
    }

    private fun addEvent(event: LiveEvent) {
        _events.value = listOf(event) + _events.value.take(49)
    }
}
