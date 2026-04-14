package com.apptolast.fiscsitmonitor.data.server.dto

import com.apptolast.fiscsitmonitor.domain.model.UserServer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserServerDto(
    val id: Int,
    val name: String,
    val host: String,
    @SerialName("api_port") val apiPort: Int,
    @SerialName("frm_http_port") val frmHttpPort: Int,
    val status: String? = null,
    @SerialName("last_seen_at") val lastSeenAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
) {
    fun toDomain(frmWsPort: Int, apiToken: String?): UserServer = UserServer(
        id = id,
        name = name,
        host = host,
        apiPort = apiPort,
        frmHttpPort = frmHttpPort,
        frmWsPort = frmWsPort,
        apiToken = apiToken,
        status = status ?: "offline",
        lastSeenAt = lastSeenAt,
        createdAt = createdAt,
    )
}
