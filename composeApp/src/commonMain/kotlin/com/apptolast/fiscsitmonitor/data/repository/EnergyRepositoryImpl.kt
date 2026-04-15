package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EnergyRepositoryImpl(
    serverRepository: ServerRepository,
) : EnergyRepository {

    override val circuits: StateFlow<List<PowerCircuitDto>> = serverRepository.powerCircuits

    private val _generators = MutableStateFlow<List<GeneratorDto>>(emptyList())
    override val generators: StateFlow<List<GeneratorDto>> = _generators.asStateFlow()

    fun updateGenerators(generators: List<GeneratorDto>) { _generators.value = generators }

    fun clear() {
        _generators.value = emptyList()
    }
}
