package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.ResourceSinkDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import com.apptolast.fiscsitmonitor.domain.repository.FactoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FactoryRepositoryImpl : FactoryRepository {

    private val _buildings = MutableStateFlow<List<FactoryBuildingDto>>(emptyList())
    override val buildings: StateFlow<List<FactoryBuildingDto>> = _buildings.asStateFlow()

    private val _extractors = MutableStateFlow<List<ExtractorDto>>(emptyList())
    override val extractors: StateFlow<List<ExtractorDto>> = _extractors.asStateFlow()

    private val _worldInventory = MutableStateFlow<List<WorldInventoryItemDto>>(emptyList())
    override val worldInventory: StateFlow<List<WorldInventoryItemDto>> = _worldInventory.asStateFlow()

    private val _resourceSink = MutableStateFlow<ResourceSinkDto?>(null)
    override val resourceSink: StateFlow<ResourceSinkDto?> = _resourceSink.asStateFlow()

    fun updateBuildings(buildings: List<FactoryBuildingDto>) { _buildings.value = buildings }
    fun updateExtractors(extractors: List<ExtractorDto>) { _extractors.value = extractors }
    fun updateWorldInventory(inventory: List<WorldInventoryItemDto>) { _worldInventory.value = inventory }
    fun updateResourceSink(sink: ResourceSinkDto) {
        _resourceSink.value = sink
    }
}
