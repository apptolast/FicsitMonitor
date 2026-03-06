package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneratorDto(
    @SerialName("Name") val name: String = "",
    @SerialName("ClassName") val className: String = "",
    @SerialName("location") val location: LocationDto? = null,
    @SerialName("BaseProd") val baseProd: Double = 0.0,
    @SerialName("DynamicProd") val dynamicProd: Double = 0.0,
    @SerialName("RegulatedDemandProd") val regulatedDemandProd: Double = 0.0,
    @SerialName("FuelAmount") val fuelAmount: Double = 0.0,
    @SerialName("PowerProduction") val powerProduction: Double = 0.0,
    @SerialName("IsProducing") val isProducing: Boolean = false,
    @SerialName("IsConsumed") val isConsumed: Boolean = false,
)

@Serializable
data class LocationDto(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val rotation: Double = 0.0,
)
