package com.apptolast.fiscsitmonitor.data.auth

import com.apptolast.fiscsitmonitor.data.auth.dto.LoginRequest
import com.apptolast.fiscsitmonitor.data.auth.dto.RegisterRequest
import com.apptolast.fiscsitmonitor.data.auth.dto.UserDto
import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.remote.Environment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class UpdateLocaleRequest(val locale: String)

class AuthApiService(
    private val client: HttpClient,
    private val environment: Environment,
) {

    suspend fun register(body: RegisterRequest): HttpResponse = client.post("${environment.currentApiBase()}register") {
        setBody(body)
    }

    suspend fun login(body: LoginRequest): HttpResponse = client.post("${environment.currentApiBase()}login") {
        setBody(body)
    }

    suspend fun logout(): HttpResponse = client.post("${environment.currentApiBase()}logout")

    suspend fun me(): UserDto =
        client.get("${environment.currentApiBase()}user").body<ApiResponse<UserDto>>().data

    suspend fun updateLocale(locale: String): UserDto =
        client.patch("${environment.currentApiV1Base()}me") {
            setBody(UpdateLocaleRequest(locale))
        }.body<ApiResponse<UserDto>>().data
}
