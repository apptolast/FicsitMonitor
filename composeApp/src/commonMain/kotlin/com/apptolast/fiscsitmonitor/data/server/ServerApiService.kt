package com.apptolast.fiscsitmonitor.data.server

import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.remote.Environment
import com.apptolast.fiscsitmonitor.data.server.dto.CreateServerRequest
import com.apptolast.fiscsitmonitor.data.server.dto.UpdateServerRequest
import com.apptolast.fiscsitmonitor.data.server.dto.UserServerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class ServerApiService(
    private val client: HttpClient,
    private val environment: Environment,
) {

    suspend fun list(): List<UserServerDto> =
        client.get("${environment.currentApiV1Base()}servers").body<ApiResponse<List<UserServerDto>>>().data

    suspend fun get(serverId: Int): UserServerDto =
        client.get("${environment.currentApiV1Base()}servers/$serverId").body<ApiResponse<UserServerDto>>().data

    suspend fun create(body: CreateServerRequest): HttpResponse =
        client.post("${environment.currentApiV1Base()}servers") { setBody(body) }

    suspend fun update(serverId: Int, body: UpdateServerRequest): HttpResponse =
        client.patch("${environment.currentApiV1Base()}servers/$serverId") { setBody(body) }

    suspend fun delete(serverId: Int): HttpResponse =
        client.delete("${environment.currentApiV1Base()}servers/$serverId")
}
