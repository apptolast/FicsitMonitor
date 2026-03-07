package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtractorDto(
    val id: String = "",
    val name: String = "",
    @SerialName("class_name") val className: String = "",
    val recipe: String = "",
    @SerialName("manu_speed") val manuSpeed: Int = 100,
    @SerialName("is_producing") val isProducing: Boolean = false,
    @SerialName("is_paused") val isPaused: Boolean = false,
    @SerialName("is_configured") val isConfigured: Boolean = false,
    @SerialName("power_consumed") val powerConsumed: Double = 0.0,
    @SerialName("max_power") val maxPower: Double = 0.0,
    @SerialName("circuit_group_id") val circuitGroupId: Int? = null,
    @SerialName("prod_name") val prodName: String = "",
    @SerialName("prod_current") val prodCurrent: Double = 0.0,
    @SerialName("prod_max") val prodMax: Double = 0.0,
    @SerialName("prod_percent") val prodPercent: Double = 0.0,
)