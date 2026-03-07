package com.apptolast.fiscsitmonitor.data.remote.api

import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SatisfactoryApiService(private val client: HttpClient) {

    suspend fun getServers(): List<ServerDto> =
        client.get("servers").body<ApiResponse<List<ServerDto>>>().data

    suspend fun getServer(serverId: Int): ServerDto =
        client.get("servers/$serverId").body<ApiResponse<ServerDto>>().data
}
