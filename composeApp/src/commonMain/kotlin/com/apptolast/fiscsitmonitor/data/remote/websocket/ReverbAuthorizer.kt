package com.apptolast.fiscsitmonitor.data.remote.websocket

import com.apptolast.fiscsitmonitor.data.remote.Environment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class AuthorizeBody(
    val channel_name: String,
    val socket_id: String,
)

@Serializable
private data class AuthorizeResponse(
    val auth: String,
    val channel_data: String? = null,
)

class ReverbAuthorizer(
    private val client: HttpClient,
    private val environment: Environment,
) {
    suspend fun authorize(channelName: String, socketId: String): String {
        val url = "${environment.currentBaseUrl()}/broadcasting/auth"
        val response: AuthorizeResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(AuthorizeBody(channel_name = channelName, socket_id = socketId))
        }.body()
        return response.auth
    }
}
