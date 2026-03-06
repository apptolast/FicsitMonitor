package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EnergyRepositoryImpl(
    private val serverRepository: ServerRepository,
) : EnergyRepository {

    override val circuits: StateFlow<List<PowerCircuitDto>> = serverRepository.powerCircuits

    private val _generators = MutableStateFlow<List<GeneratorDto>>(emptyList())
    override val generators: StateFlow<List<GeneratorDto>> = _generators.asStateFlow()

    override suspend fun refreshCircuits() {
        serverRepository.refreshPower()
    }

    override suspend fun refreshGenerators() {
        // Generators come from WebSocket or dashboard bulk endpoint
        // REST polling fallback handled by ViewModel
    }

    fun updateGenerators(generators: List<GeneratorDto>) { _generators.value = generators }
}
