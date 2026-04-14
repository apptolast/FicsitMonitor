package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.server.ServerApiService
import com.apptolast.fiscsitmonitor.data.server.dto.CreateServerRequest
import com.apptolast.fiscsitmonitor.data.server.dto.UpdateServerRequest
import com.apptolast.fiscsitmonitor.data.server.dto.UserServerDto
import com.apptolast.fiscsitmonitor.data.session.ServerShadowStore
import com.apptolast.fiscsitmonitor.domain.model.UserServer
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

private const val DEFAULT_FRM_WS_PORT = 8081

class UserServerRepositoryImpl(
    private val api: ServerApiService,
    private val shadow: ServerShadowStore,
) : UserServerRepository {

    override suspend fun list(): List<UserServer> =
        api.list().map { it.merge() }

    override suspend fun get(serverId: Int): UserServer =
        api.get(serverId).merge()

    override suspend fun create(
        name: String,
        host: String,
        apiPort: Int,
        frmHttpPort: Int,
        frmWsPort: Int,
        apiToken: String?,
    ): UserServer {
        val response = api.create(
            CreateServerRequest(
                name = name,
                host = host,
                apiPort = apiPort,
                frmHttpPort = frmHttpPort,
                frmWsPort = frmWsPort,
                apiToken = apiToken?.takeIf { it.isNotBlank() },
            ),
        )
        val dto = parseServer(response)
        shadow.put(dto.id, frmWsPort, apiToken)
        return dto.toDomain(frmWsPort = frmWsPort, apiToken = apiToken)
    }

    override suspend fun update(
        serverId: Int,
        name: String,
        host: String,
        apiPort: Int,
        frmHttpPort: Int,
        frmWsPort: Int,
        apiToken: String?,
    ): UserServer {
        val response = api.update(
            serverId = serverId,
            body = UpdateServerRequest(
                name = name,
                host = host,
                apiPort = apiPort,
                frmHttpPort = frmHttpPort,
                frmWsPort = frmWsPort,
                apiToken = apiToken?.takeIf { it.isNotBlank() },
            ),
        )
        val dto = parseServer(response)
        shadow.put(dto.id, frmWsPort, apiToken)
        return dto.toDomain(frmWsPort = frmWsPort, apiToken = apiToken)
    }

    override suspend fun delete(serverId: Int) {
        val response = api.delete(serverId)
        if (response.status != HttpStatusCode.NoContent && response.status != HttpStatusCode.OK) {
            throw AuthError.Unknown("HTTP ${response.status.value}")
        }
        shadow.remove(serverId)
    }

    private suspend fun parseServer(response: HttpResponse): UserServerDto {
        if (response.status == HttpStatusCode.UnprocessableEntity) {
            throw AuthError.Unknown("Validation error")
        }
        if (!response.status.isSuccess()) {
            throw AuthError.Unknown("HTTP ${response.status.value}")
        }
        return response.body<ApiResponse<UserServerDto>>().data
    }

    private fun UserServerDto.merge(): UserServer {
        val cached = shadow.get(id)
        return toDomain(
            frmWsPort = cached.frmWsPort ?: DEFAULT_FRM_WS_PORT,
            apiToken = cached.apiToken,
        )
    }

    private fun HttpStatusCode.isSuccess(): Boolean = value in 200..299
}
