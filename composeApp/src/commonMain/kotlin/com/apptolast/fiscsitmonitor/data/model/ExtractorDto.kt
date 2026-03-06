package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtractorDto(
    @SerialName("Name") val name: String = "",
    @SerialName("ClassName") val className: String = "",
    @SerialName("location") val location: LocationDto? = null,
    @SerialName("ItemName") val itemName: String = "",
    @SerialName("CurrentProd") val currentProd: Double = 0.0,
    @SerialName("MaxProd") val maxProd: Double = 0.0,
    @SerialName("ProdPercent") val prodPercent: Double = 0.0,
    @SerialName("IsProducing") val isProducing: Boolean = false,
)
