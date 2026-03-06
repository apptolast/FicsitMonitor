package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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
) : ViewModel() {

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
            activeTrains = trains.count { it.status == "Self-Driving" },
            itemsInProduction = production.count { it.currentProd > 0 },
            fusesTriggered = circuits.count { it.fuseTriggered },
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, LiveSummary())

    fun addEvent(event: LiveEvent) {
        _events.value = listOf(event) + _events.value.take(49)
    }
}
