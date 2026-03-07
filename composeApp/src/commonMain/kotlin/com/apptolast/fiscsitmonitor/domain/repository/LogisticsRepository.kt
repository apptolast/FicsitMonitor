package com.apptolast.fiscsitmonitor.domain.repository

import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import kotlinx.coroutines.flow.StateFlow

interface LogisticsRepository {
    val trains: StateFlow<List<TrainDto>>
    val drones: StateFlow<List<DroneStationDto>>
    val players: StateFlow<List<PlayerDto>>
}
