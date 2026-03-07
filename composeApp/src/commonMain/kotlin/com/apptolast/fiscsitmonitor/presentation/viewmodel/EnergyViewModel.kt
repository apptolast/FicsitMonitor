package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EnergyViewModel(
    energyRepository: EnergyRepository,
) : ViewModel() {

    val circuits: StateFlow<List<PowerCircuitDto>> = energyRepository.circuits
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val generators: StateFlow<List<GeneratorDto>> = energyRepository.generators
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
