package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FactoryBuildingDto(
    @SerialName("Name") val name: String = "",
    @SerialName("ClassName") val className: String = "",
    @SerialName("location") val location: LocationDto? = null,
    @SerialName("Recipe") val recipe: String = "",
    @SerialName("IsProducing") val isProducing: Boolean = false,
    @SerialName("IsPaused") val isPaused: Boolean = false,
    @SerialName("CurrentPotential") val currentPotential: Double = 0.0,
    @SerialName("PowerConsumption") val powerConsumption: Double = 0.0,
    @SerialName("Productivity") val productivity: Double = 0.0,
    @SerialName("CycleTime") val cycleTime: Double = 0.0,
)
