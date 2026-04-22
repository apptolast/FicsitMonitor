package com.apptolast.fiscsitmonitor.domain.model

data class UserServer(
    val id: Int,
    val name: String,
    val host: String,
    val scheme: String,
    val apiPort: Int,
    val pathPrefix: String,
    val verifyTls: Boolean,
    val frmHttpPort: Int,
    val frmWsPort: Int,
    val maxPlayers: Int?,
    val status: String,
    val isActive: Boolean,
    val lastSeenAt: String?,
    val createdAt: String?,
)
