package com.apptolast.fiscsitmonitor.data.server.dto

import com.apptolast.fiscsitmonitor.domain.model.UserServer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserServerDto(
    val id: Int,
    val name: String,
    val host: String,
    val scheme: String? = null,
    @SerialName("api_port") val apiPort: Int,
    @SerialName("path_prefix") val pathPrefix: String? = null,
    @SerialName("verify_tls") val verifyTls: Boolean? = null,
    @SerialName("frm_http_port") val frmHttpPort: Int,
    @SerialName("frm_ws_port") val frmWsPort: Int? = null,
    @SerialName("max_players") val maxPlayers: Int? = null,
    val status: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("last_seen_at") val lastSeenAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
) {
    fun toDomain(): UserServer = UserServer(
        id = id,
        name = name,
        host = host,
        scheme = scheme ?: DEFAULT_SCHEME,
        apiPort = apiPort,
        pathPrefix = pathPrefix ?: DEFAULT_PATH_PREFIX,
        verifyTls = verifyTls ?: false,
        frmHttpPort = frmHttpPort,
        frmWsPort = frmWsPort ?: DEFAULT_FRM_WS_PORT,
        maxPlayers = maxPlayers,
        status = status ?: "offline",
        isActive = isActive ?: true,
        lastSeenAt = lastSeenAt,
        createdAt = createdAt,
    )

    companion object {
        const val DEFAULT_SCHEME = "https"
        const val DEFAULT_PATH_PREFIX = "/api/v1"
        const val DEFAULT_FRM_WS_PORT = 8081
    }
}
