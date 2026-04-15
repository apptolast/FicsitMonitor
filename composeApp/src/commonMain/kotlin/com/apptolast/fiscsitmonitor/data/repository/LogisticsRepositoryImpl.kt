package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LogisticsRepositoryImpl(
    serverRepository: ServerRepository,
) : LogisticsRepository {

    private val _trains = MutableStateFlow<List<TrainDto>>(emptyList())
    override val trains: StateFlow<List<TrainDto>> = _trains.asStateFlow()

    private val _drones = MutableStateFlow<List<DroneStationDto>>(emptyList())
    override val drones: StateFlow<List<DroneStationDto>> = _drones.asStateFlow()

    override val players: StateFlow<List<PlayerDto>> = serverRepository.players

    fun updateTrains(trains: List<TrainDto>) { _trains.value = trains }
    fun updateDrones(drones: List<DroneStationDto>) { _drones.value = drones }

    fun clear() {
        _trains.value = emptyList()
        _drones.value = emptyList()
    }
}
