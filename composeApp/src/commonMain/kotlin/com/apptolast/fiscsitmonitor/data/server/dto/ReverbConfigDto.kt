package com.apptolast.fiscsitmonitor.data.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReverbConfigDto(
    val key: String,
    val host: String,
    val port: Int,
    val scheme: String,
)
