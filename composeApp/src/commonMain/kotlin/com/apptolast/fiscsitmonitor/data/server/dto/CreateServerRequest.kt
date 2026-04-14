package com.apptolast.fiscsitmonitor.data.server.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateServerRequest(
    val name: String,
    val host: String,
    @SerialName("api_port") val apiPort: Int? = null,
    @SerialName("frm_http_port") val frmHttpPort: Int? = null,
    @SerialName("frm_ws_port") val frmWsPort: Int? = null,
    @SerialName("api_token") val apiToken: String? = null,
)
