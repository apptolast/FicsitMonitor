package com.apptolast.fiscsitmonitor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneratorDto(
    val id: String = "",
    val name: String = "",
    @SerialName("class_name") val className: String = "",
    @SerialName("capacity_mw") val capacityMw: Double = 0.0,
    @SerialName("load_pct") val loadPct: Double = 0.0,
    @SerialName("regulated_demand_mw") val regulatedDemandMw: Double = 0.0,
    @SerialName("fuel_amount") val fuelAmount: Double = 0.0,
    @SerialName("fuel_resource") val fuelResource: String = "",
    @SerialName("can_start") val canStart: Boolean = false,
    @SerialName("is_full_speed") val isFullSpeed: Boolean = false,
    val somersloops: Int = 0,
    @SerialName("circuit_group_id") val circuitGroupId: Int? = null,
    @SerialName("fuel_inv_name") val fuelInvName: String? = null,
    @SerialName("fuel_inv_class") val fuelInvClass: String? = null,
    @SerialName("fuel_inv_amount") val fuelInvAmount: Int = 0,
    @SerialName("fuel_inv_max") val fuelInvMax: Int = 0,
    @SerialName("available_fuel") val availableFuel: List<FuelEntryDto> = emptyList(),
)

@Serializable
data class FuelEntryDto(
    val name: String = "",
    @SerialName("class") val className: String = "",
    val amount: Int = 0,
)