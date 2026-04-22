package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerDto(
    val id: Int,
    val name: String,
    val host: String,
    @SerialName("api_port") val apiPort: Int,
    @SerialName("frm_http_port") val frmHttpPort: Int,
    @SerialName("max_players") val maxPlayers: Int? = null,
    val status: String,
    @SerialName("last_seen_at") val lastSeenAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)
