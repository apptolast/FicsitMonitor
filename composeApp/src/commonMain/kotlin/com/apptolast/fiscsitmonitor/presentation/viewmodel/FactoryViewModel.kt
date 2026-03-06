package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import com.apptolast.fiscsitmonitor.domain.repository.FactoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FactoryViewModel(
    private val factoryRepository: FactoryRepository,
) : ViewModel() {

    val buildings: StateFlow<List<FactoryBuildingDto>> = factoryRepository.buildings
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val extractors: StateFlow<List<ExtractorDto>> = factoryRepository.extractors
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val worldInventory: StateFlow<List<WorldInventoryItemDto>> = factoryRepository.worldInventory
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
