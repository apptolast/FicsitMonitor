package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerMetricsDto(
    @SerialName("tick_rate") val tickRate: Double = 0.0,
    @SerialName("player_count") val playerCount: Int = 0,
    @SerialName("tech_tier") val techTier: Int = 0,
    @SerialName("game_phase") val gamePhase: String? = null,
    @SerialName("is_running") val isRunning: Boolean = false,
    @SerialName("is_paused") val isPaused: Boolean = false,
    @SerialName("total_duration") val totalDuration: Int = 0,
    val time: String? = null,
)
