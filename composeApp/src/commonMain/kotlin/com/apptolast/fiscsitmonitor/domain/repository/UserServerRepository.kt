package com.apptolast.fiscsitmonitor.domain.repository

import com.apptolast.fiscsitmonitor.domain.model.UserServer

interface UserServerRepository {
    suspend fun list(): List<UserServer>
    suspend fun get(serverId: Int): UserServer
    suspend fun create(
        name: String,
        host: String,
        apiPort: Int,
        frmHttpPort: Int,
        frmWsPort: Int,
        adminPassword: String,
    ): UserServer

    suspend fun update(
        serverId: Int,
        name: String,
        host: String,
        apiPort: Int,
        frmHttpPort: Int,
        frmWsPort: Int,
    ): UserServer

    suspend fun delete(serverId: Int)
}
