package com.apptolast.fiscsitmonitor.domain.model

data class UserServer(
    val id: Int,
    val name: String,
    val host: String,
    val apiPort: Int,
    val frmHttpPort: Int,
    val frmWsPort: Int,
    val status: String,
    val lastSeenAt: String?,
    val createdAt: String?,
)
