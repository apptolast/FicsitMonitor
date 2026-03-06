package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DroneStationDto(
    val id: Int,
    @SerialName("frm_id") val frmId: String = "",
    val name: String = "",
    @SerialName("drone_status") val droneStatus: String = "",
    @SerialName("paired_station") val pairedStation: String = "",
    @SerialName("avg_round_trip_secs") val avgRoundTripSecs: Double = 0.0,
    @SerialName("avg_inc_rate") val avgIncRate: Double = 0.0,
    @SerialName("avg_out_rate") val avgOutRate: Double = 0.0,
    @SerialName("power_consumed") val powerConsumed: Double = 0.0,
    @SerialName("updated_at") val updatedAt: String? = null,
)
