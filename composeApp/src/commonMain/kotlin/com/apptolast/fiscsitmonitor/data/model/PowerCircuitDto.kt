package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PowerCircuitDto(
    @SerialName("circuit_group_id") val circuitGroupId: Int = 0,
    @SerialName("power_production") val powerProduction: Double = 0.0,
    @SerialName("power_consumed") val powerConsumed: Double = 0.0,
    @SerialName("power_capacity") val powerCapacity: Double = 0.0,
    @SerialName("power_max_consumed") val powerMaxConsumed: Double = 0.0,
    @SerialName("battery_percent") val batteryPercent: Double = 0.0,
    @SerialName("battery_capacity") val batteryCapacity: Double = 0.0,
    @SerialName("battery_differential") val batteryDifferential: Double = 0.0,
    @SerialName("fuse_triggered") val fuseTriggered: Boolean = false,
    val time: String? = null,
)
