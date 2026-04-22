package com.apptolast.fiscsitmonitor.data.server.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateServerRequest(
    val name: String,
    val host: String,
    val scheme: String? = null,
    @SerialName("api_port") val apiPort: Int? = null,
    @SerialName("path_prefix") val pathPrefix: String? = null,
    @SerialName("verify_tls") val verifyTls: Boolean? = null,
    @SerialName("frm_http_port") val frmHttpPort: Int? = null,
    @SerialName("frm_ws_port") val frmWsPort: Int? = null,
    @SerialName("admin_password") val adminPassword: String,
)
