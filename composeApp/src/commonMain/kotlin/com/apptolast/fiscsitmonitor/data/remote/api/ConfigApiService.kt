package com.apptolast.fiscsitmonitor.data.remote.api

import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.remote.Environment
import com.apptolast.fiscsitmonitor.data.server.dto.ReverbConfigDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ConfigApiService(
    private val client: HttpClient,
    private val environment: Environment,
) {
    suspend fun reverbConfig(): ReverbConfigDto =
        client.get("${environment.currentApiV1Base()}config/reverb")
            .body<ApiResponse<ReverbConfigDto>>().data
}
