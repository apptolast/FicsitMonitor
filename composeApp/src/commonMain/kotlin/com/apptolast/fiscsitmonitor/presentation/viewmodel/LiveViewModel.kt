package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val title: String,
    val subtitle: String,
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
                val icon = when (wsEvent.type) {
                    "power" -> "\u26A1"
                    "factory" -> "\uD83C\uDFED"
                    "trains" -> "\uD83D\uDE82"
                    "drones" -> "\u2708\uFE0F"
                    "players" -> "\uD83D\uDC64"
                    "production" -> "\u2699\uFE0F"
                    "generators" -> "\uD83D\uDD25"
                    "extractors" -> "\u26CF\uFE0F"
                    "server" -> "\uD83D\uDDA5\uFE0F"
                    "metrics" -> "\uD83D\uDCCA"
                    "inventory" -> "\uD83D\uDCE6"
                    "sink" -> "\u2B50"
                    else -> "\uD83D\uDD14"
                }
                val elapsed = startMark.elapsedNow()
                val totalSecs = elapsed.inWholeSeconds
                val h = (totalSecs / 3600).toString().padStart(2, '0')
                val m = ((totalSecs % 3600) / 60).toString().padStart(2, '0')
                val s = (totalSecs % 60).toString().padStart(2, '0')
                val timestamp = "+$h:$m:$s"
                addEvent(
                    LiveEvent(
                        icon = icon,
                        title = wsEvent.type.replaceFirstChar { it.uppercase() },
                        subtitle = wsEvent.summary,
                        timestamp = timestamp,
                    )
                )
            }
        }
    }

    private fun addEvent(event: LiveEvent) {
        _events.value = listOf(event) + _events.value.take(49)
    }
}
