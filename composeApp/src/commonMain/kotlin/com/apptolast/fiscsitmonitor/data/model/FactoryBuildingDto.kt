package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FactoryBuildingDto(
    val id: String = "",
    val name: String = "",
    val recipe: String? = null,
    @SerialName("manu_speed") val manuSpeed: Double = 100.0,
    @SerialName("is_configured") val isConfigured: Boolean = false,
    @SerialName("is_producing") val isProducing: Boolean = false,
    @SerialName("is_paused") val isPaused: Boolean = false,
    @SerialName("power_consumed") val powerConsumed: Double = 0.0,
    @SerialName("circuit_group_id") val circuitGroupId: Int? = null,
    val somersloops: Int = 0,
    val production: List<BuildingProductionDto> = emptyList(),
)

@Serializable
data class BuildingProductionDto(
    val name: String = "",
    @SerialName("current_prod") val currentProd: Double = 0.0,
    @SerialName("max_prod") val maxProd: Double = 0.0,
    @SerialName("prod_percent") val prodPercent: Double = 0.0,
)