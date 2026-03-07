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
    @SerialName("battery_percent") val batteryPercent: Double? = null,
    @SerialName("battery_capacity") val batteryCapacity: Double? = null,
    @SerialName("battery_differential") val batteryDifferential: Double? = null,
    @SerialName("battery_time_empty") val batteryTimeEmpty: String? = null,
    @SerialName("battery_time_full") val batteryTimeFull: String? = null,
    @SerialName("fuse_triggered") val fuseTriggered: Boolean = false,
    val time: String? = null,
)
