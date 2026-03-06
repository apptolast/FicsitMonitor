package com.apptolast.fiscsitmonitor.data.remote.api

import com.apptolast.fiscsitmonitor.data.model.ApiResponse
import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SatisfactoryApiService(private val client: HttpClient) {

    suspend fun getServers(): List<ServerDto> =
        client.get("servers").body<ApiResponse<List<ServerDto>>>().data

    suspend fun getServer(serverId: Int): ServerDto =
        client.get("servers/$serverId").body<ApiResponse<ServerDto>>().data

    suspend fun getServerMetrics(serverId: Int): ServerMetricsDto =
        client.get("servers/$serverId/metrics/latest").body()

    suspend fun getPlayers(serverId: Int): List<PlayerDto> =
        client.get("servers/$serverId/players").body<ApiResponse<List<PlayerDto>>>().data

    suspend fun getPowerMetrics(serverId: Int): List<PowerCircuitDto> =
        client.get("servers/$serverId/power/latest").body<ApiResponse<List<PowerCircuitDto>>>().data

    suspend fun getProductionMetrics(serverId: Int): List<ProductionItemDto> =
        client.get("servers/$serverId/production/latest").body<ApiResponse<List<ProductionItemDto>>>().data

    suspend fun getTrains(serverId: Int): List<TrainDto> =
        client.get("servers/$serverId/trains").body<ApiResponse<List<TrainDto>>>().data

    suspend fun getDrones(serverId: Int): List<DroneStationDto> =
        client.get("servers/$serverId/drones").body<ApiResponse<List<DroneStationDto>>>().data
}
