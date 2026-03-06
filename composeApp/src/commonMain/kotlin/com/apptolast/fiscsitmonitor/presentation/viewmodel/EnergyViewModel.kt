package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnergyViewModel(
    private val energyRepository: EnergyRepository,
) : ViewModel() {

    val circuits: StateFlow<List<PowerCircuitDto>> = energyRepository.circuits
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val generators: StateFlow<List<GeneratorDto>> = energyRepository.generators
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                energyRepository.refreshCircuits()
            } catch (_: Exception) { }
            _isLoading.value = false
        }
    }
}
