package com.apptolast.fiscsitmonitor.presentation.ui.screens.logistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.apptolast.fiscsitmonitor.presentation.ui.components.BadgeType
import com.apptolast.fiscsitmonitor.presentation.ui.components.DroneCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.EmptyState
import com.apptolast.fiscsitmonitor.presentation.ui.components.PlayerCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.components.TrainCard
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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
            title = "LOGISTICS",
            icon = Icons.Default.LocalShipping,
            badgeText = "EVERY 30S",
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Trains", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }
                Text("${trains.size} total", style = MaterialTheme.typography.bodyMedium, color = FicsitTheme.colors.textSecondary)
            }
            if (trains.isEmpty()) {
                EmptyState(icon = Icons.Default.Train, message = "No trains registered")
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
                Text("Drones", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                Text("${drones.size} stations", style = MaterialTheme.typography.bodyMedium, color = FicsitTheme.colors.textSecondary)
            }
            if (drones.isEmpty()) {
                EmptyState(icon = Icons.Default.Flight, message = "No drones registered")
            } else {
                drones.forEach { drone -> DroneCard(station = drone) }
            }
        }

        // Players
        val onlineCount = players.count { it.isOnline }
        SectionHeader(
            title = "PLAYERS ($onlineCount)",
            icon = Icons.Default.Group,
            iconTint = FicsitTheme.colors.accentPurple,
            badgeText = "EVERY 15S",
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
                EmptyState(icon = Icons.Default.SentimentDissatisfied, message = "No player online")
            }
        } else {
            players.forEach { player ->
                PlayerCard(player = player)
            }
        }
    }
}
