package com.apptolast.fiscsitmonitor.domain.repository

import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import kotlinx.coroutines.flow.StateFlow

interface FactoryRepository {
    val buildings: StateFlow<List<FactoryBuildingDto>>
    val extractors: StateFlow<List<ExtractorDto>>
    val worldInventory: StateFlow<List<WorldInventoryItemDto>>

    suspend fun refreshBuildings()
    suspend fun refreshExtractors()
    suspend fun refreshWorldInventory()
}
