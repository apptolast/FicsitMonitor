package com.apptolast.fiscsitmonitor.presentation.ui.screens.logistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.presentation.ui.components.DroneCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.EmptyState
import com.apptolast.fiscsitmonitor.presentation.ui.components.PlayerCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.components.TrainCard
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.badge_every_15s
import ficsitmonitor.composeapp.generated.resources.badge_every_30s
import ficsitmonitor.composeapp.generated.resources.empty_drones
import ficsitmonitor.composeapp.generated.resources.empty_players
import ficsitmonitor.composeapp.generated.resources.empty_trains
import ficsitmonitor.composeapp.generated.resources.label_drones
import ficsitmonitor.composeapp.generated.resources.label_trains_section
import ficsitmonitor.composeapp.generated.resources.section_logistics
import ficsitmonitor.composeapp.generated.resources.section_players
import ficsitmonitor.composeapp.generated.resources.stations_format
import ficsitmonitor.composeapp.generated.resources.total_format
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogisticsContent(
    trains: List<TrainDto>,
    drones: List<DroneStationDto>,
    players: List<PlayerDto>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SectionHeader(
            title = stringResource(Res.string.section_logistics),
            icon = Icons.Default.LocalShipping,
            badgeText = stringResource(Res.string.badge_every_30s),
        )

        // Trains
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(FicsitTheme.colors.bgCard)
                .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(Res.string.label_trains_section), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                Text(stringResource(Res.string.total_format, trains.size), style = MaterialTheme.typography.bodyMedium, color = FicsitTheme.colors.textSecondary)
            }
            if (trains.isEmpty()) {
                EmptyState(icon = Icons.Default.Train, message = stringResource(Res.string.empty_trains))
            } else {
                trains.forEach { train -> TrainCard(train = train) }
            }
        }

        // Drones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(FicsitTheme.colors.bgCard)
                .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(Res.string.label_drones), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                Text(stringResource(Res.string.stations_format, drones.size), style = MaterialTheme.typography.bodyMedium, color = FicsitTheme.colors.textSecondary)
            }
            if (drones.isEmpty()) {
                EmptyState(icon = Icons.Default.Flight, message = stringResource(Res.string.empty_drones))
            } else {
                drones.forEach { drone -> DroneCard(station = drone) }
            }
        }

        // Players
        val onlineCount = players.count { it.isOnline }
        SectionHeader(
            title = stringResource(Res.string.section_players, onlineCount),
            icon = Icons.Default.Group,
            iconTint = FicsitTheme.colors.accentPurple,
            badgeText = stringResource(Res.string.badge_every_15s),
        )

        if (players.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(FicsitTheme.colors.bgCard)
                    .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
                    .padding(16.dp),
            ) {
                EmptyState(icon = Icons.Default.SentimentDissatisfied, message = stringResource(Res.string.empty_players))
            }
        } else {
            players.forEach { player ->
                PlayerCard(player = player)
            }
        }
    }
}

@Preview
@Composable
private fun LogisticsContentPreview() {
    FicsitMonitorTheme {
        LogisticsContent(
            trains = listOf(
                TrainDto(
                    frmId = "train1",
                    name = "Iron Express",
                    selfDriving = true,
                    currentStation = "Iron Depot",
                    forwardSpeed = 120.0,
                ),
                TrainDto(
                    frmId = "train2",
                    name = "Coal Runner",
                    isDerailed = true,
                    forwardSpeed = 0.0,
                ),
            ),
            drones = listOf(
                DroneStationDto(
                    frmId = "drone1",
                    name = "Copper Outpost",
                    droneStatus = "En Route",
                    pairedStation = "Main Base",
                    avgRoundTripSecs = 45.0,
                ),
            ),
            players = listOf(
                PlayerDto(id = 1, name = "Pioneer_1", isOnline = true, health = 100.0),
                PlayerDto(id = 2, name = "Pioneer_2", isOnline = false, health = 50.0),
            ),
        )
    }
}
