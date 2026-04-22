package com.apptolast.fiscsitmonitor.data.repository

import com.apptolast.fiscsitmonitor.data.auth.AuthApiService
import com.apptolast.fiscsitmonitor.data.auth.dto.AuthResponse
import com.apptolast.fiscsitmonitor.data.auth.dto.LoginRequest
import com.apptolast.fiscsitmonitor.data.auth.dto.RegisterRequest
import com.apptolast.fiscsitmonitor.data.auth.dto.ValidationErrorResponse
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.domain.model.User
import com.apptolast.fiscsitmonitor.domain.repository.AuthRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import com.apptolast.fiscsitmonitor.platform.platformDeviceName
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val session: AuthSession,
) : AuthRepository {

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ): User {
        val response = runCatchingNetwork {
            api.register(
                RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    passwordConfirmation = passwordConfirmation,
                    deviceName = platformDeviceName(),
                ),
            )
        }
        return handleAuthResponse(response)
    }

    override suspend fun login(email: String, password: String): User {
        val response = runCatchingNetwork {
            api.login(LoginRequest(email = email, password = password, deviceName = platformDeviceName()))
        }
        if (response.status == HttpStatusCode.UnprocessableEntity) {
            val errors = parseValidationErrors(response)
            if (errors.containsKey("email")) throw AuthError.InvalidCredentials
            throw AuthError.Validation(errors)
        }
        return handleAuthResponse(response)
    }

    override suspend fun logout() {
        runCatching { api.logout() }
        session.onLogout()
    }

    override suspend fun refreshCurrentUser(): User? = runCatching {
        api.me().toDomain().also { session.onLogin(session.currentToken() ?: return null, it) }
    }.getOrNull()

    override suspend fun updateLocale(locale: String): User {
        val updated = runCatchingNetwork { api.updateLocale(locale) }.toDomain()
        session.setLocale(locale)
        val token = session.currentToken()
        if (token != null) session.onLogin(token, updated)
        return updated
    }

    private suspend fun handleAuthResponse(response: HttpResponse): User {
        when (response.status) {
            HttpStatusCode.OK, HttpStatusCode.Created -> {
                val body = response.body<AuthResponse>()
                val user = body.user.toDomain()
                session.onLogin(body.token, user)
                return user
            }

            HttpStatusCode.UnprocessableEntity -> {
                throw AuthError.Validation(parseValidationErrors(response))
            }

            else -> throw AuthError.Unknown("HTTP ${response.status.value}")
        }
    }

    private suspend fun parseValidationErrors(response: HttpResponse): Map<String, List<String>> =
        runCatching { response.body<ValidationErrorResponse>().errors }.getOrDefault(emptyMap())

    private inline fun <T> runCatchingNetwork(block: () -> T): T = try {
        block()
    } catch (error: AuthError) {
        throw error
    } catch (t: Throwable) {
        throw AuthError.Network
    }
}
