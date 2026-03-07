package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceSinkDto(
    val name: String = "",
    @SerialName("total_points") val totalPoints: Long = 0,
    @SerialName("points_to_coupon") val pointsToCoupon: Long = 0,
    @SerialName("num_coupon") val numCoupon: Int = 0,
    val percent: Double = 0.0,
    @SerialName("graph_points") val graphPoints: List<Long> = emptyList(),
)
