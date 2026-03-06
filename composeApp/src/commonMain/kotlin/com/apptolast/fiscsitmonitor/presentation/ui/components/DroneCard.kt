package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.DroneStationDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatDecimal
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.fallback_drone_station
import ficsitmonitor.composeapp.generated.resources.idle
import ficsitmonitor.composeapp.generated.resources.label_paired
import ficsitmonitor.composeapp.generated.resources.label_round_trip
import ficsitmonitor.composeapp.generated.resources.no_data
import org.jetbrains.compose.resources.stringResource

@Composable
fun DroneCard(
    station: DroneStationDto,
    modifier: Modifier = Modifier,
) {
    val noData = stringResource(Res.string.no_data)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(FicsitTheme.colors.bgCard)
            .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(station.name.ifEmpty { stringResource(Res.string.fallback_drone_station) }, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            StatusBadge(text = station.droneStatus.uppercase().ifEmpty { stringResource(Res.string.idle) }, type = BadgeType.INFO)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_paired), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(station.pairedStation.ifEmpty { noData }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_round_trip), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text("${station.avgRoundTripSecs.formatDecimal(0)}s", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
