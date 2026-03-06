package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorldInventoryItemDto(
    @SerialName("Name") val name: String = "",
    @SerialName("ClassName") val className: String = "",
    @SerialName("Amount") val amount: Int = 0,
    @SerialName("Max") val max: Int = 0,
)
