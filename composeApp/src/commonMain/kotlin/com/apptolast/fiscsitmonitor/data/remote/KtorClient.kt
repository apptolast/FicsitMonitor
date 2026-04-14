package com.apptolast.fiscsitmonitor.data.remote

import com.apptolast.fiscsitmonitor.data.session.AuthSession
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(
    environment: Environment,
    session: AuthSession,
): HttpClient = HttpClient {
    expectSuccess = false

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
                coerceInputValues = true
                explicitNulls = false
            },
        )
    }

    install(Logging) {
        level = LogLevel.INFO
    }

    install(WebSockets)

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 30_000
    }

    defaultRequest {
        contentType(ContentType.Application.Json)
        headers.append(HttpHeaders.Accept, ContentType.Application.Json.toString())
        session.currentToken()?.let { token ->
            headers.append(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    HttpResponseValidator {
        validateResponse { response: HttpResponse ->
            if (response.status == HttpStatusCode.Unauthorized) {
                val path = response.request.url.encodedPath
                val isAuthPath = path.endsWith("/api/login") || path.endsWith("/api/register")
                if (!isAuthPath) {
                    session.onLogout()
                }
            }
        }
    }
}
