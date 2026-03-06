package com.apptolast.fiscsitmonitor.domain.repository

import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import kotlinx.coroutines.flow.StateFlow

interface EnergyRepository {
    val circuits: StateFlow<List<PowerCircuitDto>>
    val generators: StateFlow<List<GeneratorDto>>

    suspend fun refreshCircuits()
    suspend fun refreshGenerators()
}
