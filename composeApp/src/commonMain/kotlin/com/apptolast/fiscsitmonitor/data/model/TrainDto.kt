package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainDto(
    @SerialName("frm_id") val frmId: String = "",
    val name: String = "",
    val status: String? = null,
    @SerialName("forward_speed") val forwardSpeed: Double = 0.0,
    @SerialName("payload_mass") val payloadMass: Double = 0.0,
    @SerialName("max_payload_mass") val maxPayloadMass: Double = 0.0,
    @SerialName("is_derailed") val isDerailed: Boolean = false,
    @SerialName("is_pending_derail") val isPendingDerail: Boolean = false,
    @SerialName("current_station") val currentStation: String? = null,
    @SerialName("self_driving") val selfDriving: Boolean? = null,
    @SerialName("power_consumed") val powerConsumed: Double = 0.0,
    @SerialName("updated_at") val updatedAt: String? = null,
)
