package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.auth.dto.ValidationErrorResponse
import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.server.ServerApiService
import com.apptolast.fiscsitmonitor.data.server.dto.CreateServerRequest
import com.apptolast.fiscsitmonitor.data.server.dto.UpdateServerRequest
import com.apptolast.fiscsitmonitor.data.server.dto.UserServerDto
import com.apptolast.fiscsitmonitor.domain.model.UserServer
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

private const val PROVISIONING_FAILED_CODE = "provisioning_failed"

class UserServerRepositoryImpl(
    private val api: ServerApiService,
) : UserServerRepository {

    override suspend fun list(): List<UserServer> =
        api.list().map { it.toDomain() }

    override suspend fun get(serverId: Int): UserServer =
        api.get(serverId).toDomain()

    override suspend fun create(
        name: String,
        host: String,
        scheme: String,
        apiPort: Int,
        pathPrefix: String,
        verifyTls: Boolean,
        frmHttpPort: Int,
        frmWsPort: Int,
        adminPassword: String,
    ): UserServer {
        val response = api.create(
            CreateServerRequest(
                name = name,
                host = host,
                scheme = scheme,
                apiPort = apiPort,
                pathPrefix = pathPrefix,
                verifyTls = verifyTls,
                frmHttpPort = frmHttpPort,
                frmWsPort = frmWsPort,
                adminPassword = adminPassword,
            ),
        )
        return parseServer(response).toDomain()
    }

    override suspend fun update(
        serverId: Int,
        name: String,
        host: String,
        scheme: String,
        apiPort: Int,
        pathPrefix: String,
        verifyTls: Boolean,
        frmHttpPort: Int,
        frmWsPort: Int,
    ): UserServer {
        val response = api.update(
            serverId = serverId,
            body = UpdateServerRequest(
                name = name,
                host = host,
                scheme = scheme,
                apiPort = apiPort,
                pathPrefix = pathPrefix,
                verifyTls = verifyTls,
                frmHttpPort = frmHttpPort,
                frmWsPort = frmWsPort,
            ),
        )
        return parseServer(response).toDomain()
    }

    override suspend fun delete(serverId: Int) {
        val response = api.delete(serverId)
        if (response.status != HttpStatusCode.NoContent && response.status != HttpStatusCode.OK) {
            throw AuthError.Unknown("HTTP ${response.status.value}")
        }
    }

    private suspend fun parseServer(response: HttpResponse): UserServerDto {
        when (response.status) {
            HttpStatusCode.UnprocessableEntity,
            HttpStatusCode.BadGateway -> throw response.toValidationError()
            // Backend wraps any PasswordLogin / RunCommand failure from the Satisfactory server
            // as a 500 ProvisioningFailedException. The most common cause (by far) is a wrong
            // admin password, so we surface it as a field error on admin_password.
            HttpStatusCode.InternalServerError -> throw AuthError.Validation(
                mapOf("admin_password" to listOf(PROVISIONING_FAILED_CODE)),
            )
        }
        if (!response.status.isSuccess()) {
            throw AuthError.Unknown("HTTP ${response.status.value}")
        }
        return response.body<ApiResponse<UserServerDto>>().data
    }

    private suspend fun HttpResponse.toValidationError(): AuthError.Validation {
        val payload = runCatching { body<ValidationErrorResponse>() }.getOrNull()
        return AuthError.Validation(payload?.errors.orEmpty())
    }

    private fun HttpStatusCode.isSuccess(): Boolean = value in 200..299
}
