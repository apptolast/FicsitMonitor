package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class LogisticsViewModel(
    logisticsRepository: LogisticsRepository,
) : ViewModel() {

    val trains: StateFlow<List<TrainDto>> = logisticsRepository.trains
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val drones: StateFlow<List<DroneStationDto>> = logisticsRepository.drones
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val players: StateFlow<List<PlayerDto>> = logisticsRepository.players
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
