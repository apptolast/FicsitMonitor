package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainDto(
    val id: Int,
    @SerialName("frm_id") val frmId: String = "",
    val name: String = "",
    val status: String = "",
    @SerialName("forward_speed") val forwardSpeed: Double = 0.0,
    @SerialName("payload_mass") val payloadMass: Double = 0.0,
    @SerialName("max_payload_mass") val maxPayloadMass: Double = 0.0,
    @SerialName("is_derailed") val isDerailed: Boolean = false,
    @SerialName("is_pending_derail") val isPendingDerail: Boolean = false,
    @SerialName("current_station") val currentStation: String = "",
    @SerialName("self_driving") val selfDriving: Boolean = false,
    @SerialName("power_consumed") val powerConsumed: Double = 0.0,
    @SerialName("updated_at") val updatedAt: String? = null,
)
