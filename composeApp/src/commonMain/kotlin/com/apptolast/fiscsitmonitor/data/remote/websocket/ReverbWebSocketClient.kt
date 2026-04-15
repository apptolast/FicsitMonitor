package com.apptolast.fiscsitmonitor.data.remote.websocket

import com.apptolast.fiscsitmonitor.data.remote.api.ConfigApiService
import com.apptolast.fiscsitmonitor.data.server.dto.ReverbConfigDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

enum class ConnectionState { CONNECTED, RECONNECTING, DISCONNECTED }

data class WebSocketEvent(
    val event: String,
    val channel: String,
    val data: JsonObject,
)

class ReverbWebSocketClient(
    private val httpClient: HttpClient,
    private val configApi: ConfigApiService,
    private val authorizer: ReverbAuthorizer,
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true }
    private val tag = "WS_CLIENT"

    private var session: WebSocketSession? = null
    private var connectionJob: Job? = null
    private var subscribedChannel: String? = null
    private var cachedConfig: ReverbConfigDto? = null

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _events = MutableSharedFlow<WebSocketEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<WebSocketEvent> = _events.asSharedFlow()

    fun connect(serverId: Int) {
        connectionJob?.cancel()
        connectionJob = scope.launch {
            val channel = "private-servers.$serverId"
            subscribedChannel = channel
            var retryDelay = 1000L
            println("[$tag] Connecting to channel: $channel")

            while (isActive) {
                try {
                    _connectionState.value = ConnectionState.RECONNECTING
                    openConnection(channel)
                    retryDelay = 1000L
                } catch (e: Exception) {
                    println("[$tag] Connection error: ${e.message}")
                    _connectionState.value = ConnectionState.DISCONNECTED
                }

                if (!isActive) break
                println("[$tag] Reconnecting in ${retryDelay}ms...")
                delay(retryDelay)
                retryDelay = (retryDelay * 2).coerceAtMost(30_000L)
            }
        }
    }

    fun disconnect() {
        println("[$tag] Disconnecting...")
        connectionJob?.cancel()
        connectionJob = null
        subscribedChannel = null
        scope.launch {
            session?.close()
            session = null
            _connectionState.value = ConnectionState.DISCONNECTED
        }
    }

    fun invalidateConfigCache() {
        cachedConfig = null
    }

    private suspend fun openConnection(channel: String) {
        val config = loadConfig()
        val wsUrl = buildWsUrl(config)
        println("[$tag] Opening WebSocket: $wsUrl")

        session = httpClient.webSocketSession(wsUrl)
        val currentSession = session ?: return
        println("[$tag] WebSocket session opened")

        try {
            for (frame in currentSession.incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    println("[$tag] Frame received: ${text.take(200)}")
                    handleFrame(text, channel)
                }
            }
            println("[$tag] Incoming channel closed")
        } finally {
            session = null
            _connectionState.value = ConnectionState.DISCONNECTED
        }
    }

    private suspend fun handleFrame(text: String, channel: String) {
        val jsonObj = try {
            json.parseToJsonElement(text).jsonObject
        } catch (e: Exception) {
            println("[$tag] Failed to parse frame JSON: ${e.message}")
            return
        }

        val event = jsonObj["event"]?.jsonPrimitive?.content ?: return

        when (event) {
            "pusher:connection_established" -> {
                val dataStr = jsonObj["data"]?.jsonPrimitive?.content ?: ""
                println("[$tag] Connection established: $dataStr")
                _connectionState.value = ConnectionState.CONNECTED
                val socketId = extractSocketId(dataStr)
                if (socketId == null) {
                    println("[$tag] socket_id missing in connection_established payload")
                    return
                }
                subscribePrivate(channel, socketId)
            }
            "pusher_internal:subscription_succeeded" -> {
                println("[$tag] Subscription succeeded for channel: $channel")
            }
            "pusher:error" -> {
                println("[$tag] Pusher error: $jsonObj")
            }
            "pusher:ping" -> {
                println("[$tag] Ping received, sending pong")
                sendPong()
            }
            else -> {
                val eventChannel = jsonObj["channel"]?.jsonPrimitive?.content ?: return
                val dataStr = jsonObj["data"]?.jsonPrimitive?.content ?: return
                val dataObj = try {
                    json.parseToJsonElement(dataStr).jsonObject
                } catch (e: Exception) {
                    println("[$tag] Failed to parse event data for '$event': ${e.message}")
                    return
                }
                println("[$tag] Event: $event | channel: $eventChannel | keys: ${dataObj.keys}")
                _events.emit(WebSocketEvent(event, eventChannel, dataObj))
            }
        }
    }

    private suspend fun subscribePrivate(channel: String, socketId: String) {
        println("[$tag] Authorizing private channel: $channel with socket: $socketId")
        val auth = try {
            authorizer.authorize(channelName = channel, socketId = socketId)
        } catch (e: Exception) {
            println("[$tag] Authorizer failed: ${e.message}")
            session?.close()
            return
        }
        val message = buildJsonObject {
            put("event", "pusher:subscribe")
            put(
                "data",
                buildJsonObject {
                    put("auth", auth)
                    put("channel", channel)
                },
            )
        }
        session?.send(Frame.Text(message.toString()))
    }

    private suspend fun sendPong() {
        val message = buildJsonObject {
            put("event", "pusher:pong")
            put("data", buildJsonObject {})
        }
        session?.send(Frame.Text(message.toString()))
    }

    private fun extractSocketId(dataStr: String): String? = runCatching {
        val inner = json.parseToJsonElement(dataStr).jsonObject
        inner["socket_id"]?.jsonPrimitive?.content
    }.getOrNull()

    private suspend fun loadConfig(): ReverbConfigDto {
        cachedConfig?.let { return it }
        val config = configApi.reverbConfig()
        cachedConfig = config
        println("[$tag] Loaded Reverb config: host=${config.host} port=${config.port} scheme=${config.scheme}")
        return config
    }

    private fun buildWsUrl(config: ReverbConfigDto): String {
        val wsScheme = if (config.scheme.equals("https", ignoreCase = true)) "wss" else "ws"
        return "$wsScheme://${config.host}:${config.port}/app/${config.key}?protocol=7&client=kotlin&version=1.0.0&flash=false"
    }
}
