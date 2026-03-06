package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionItemDto(
    @SerialName("item_class_name") val itemClassName: String = "",
    @SerialName("item_name") val itemName: String = "",
    @SerialName("item_type") val itemType: String = "",
    @SerialName("current_prod") val currentProd: Double = 0.0,
    @SerialName("max_prod") val maxProd: Double = 0.0,
    @SerialName("current_consumed") val currentConsumed: Double = 0.0,
    @SerialName("max_consumed") val maxConsumed: Double = 0.0,
    @SerialName("prod_percent") val prodPercent: Double = 0.0,
    @SerialName("cons_percent") val consPercent: Double = 0.0,
    val time: String? = null,
)
