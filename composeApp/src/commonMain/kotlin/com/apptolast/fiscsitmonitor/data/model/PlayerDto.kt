package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: Int,
    val name: String,
    @SerialName("is_online") val isOnline: Boolean = false,
    val health: Double = 0.0,
    val speed: Double = 0.0,
    @SerialName("is_dead") val isDead: Boolean = false,
    @SerialName("pos_x") val posX: Double = 0.0,
    @SerialName("pos_y") val posY: Double = 0.0,
    @SerialName("pos_z") val posZ: Double = 0.0,
    @SerialName("last_seen_at") val lastSeenAt: String? = null,
)
