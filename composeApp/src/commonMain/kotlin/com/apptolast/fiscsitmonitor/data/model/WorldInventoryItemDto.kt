package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorldInventoryItemDto(
    val name: String = "",
    @SerialName("class_name") val className: String = "",
    val amount: Int = 0,
    @SerialName("max_amount") val maxAmount: Int = 0,
)