package com.apptolast.fiscsitmonitor.data.remote.api

import com.apptolast.fiscsitmonitor.data.remote.Environment
import com.apptolast.fiscsitmonitor.data.server.dto.DashboardSnapshotDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DashboardApiService(
    private val client: HttpClient,
    private val environment: Environment,
) {
    suspend fun snapshot(serverId: Int): DashboardSnapshotDto =
        client.get("${environment.currentApiV1Base()}servers/$serverId/dashboard").body()
}
