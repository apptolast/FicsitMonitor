package com.apptolast.fiscsitmonitor.data.server.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateServerRequest(
    val name: String? = null,
    val host: String? = null,
    val scheme: String? = null,
    @SerialName("api_port") val apiPort: Int? = null,
    @SerialName("path_prefix") val pathPrefix: String? = null,
    @SerialName("verify_tls") val verifyTls: Boolean? = null,
    @SerialName("frm_http_port") val frmHttpPort: Int? = null,
    @SerialName("frm_ws_port") val frmWsPort: Int? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
)
