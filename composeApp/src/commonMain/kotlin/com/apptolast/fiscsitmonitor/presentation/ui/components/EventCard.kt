package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.remote.websocket.LiveWsEvent
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveEvent
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.live_event_drones_updated
import ficsitmonitor.composeapp.generated.resources.live_event_extractors_updated
import ficsitmonitor.composeapp.generated.resources.live_event_factory_updated
import ficsitmonitor.composeapp.generated.resources.live_event_fuse_triggered_format
import ficsitmonitor.composeapp.generated.resources.live_event_generators_updated
import ficsitmonitor.composeapp.generated.resources.live_event_inventory_updated
import ficsitmonitor.composeapp.generated.resources.live_event_metrics_updated
import ficsitmonitor.composeapp.generated.resources.live_event_players_online_format
import ficsitmonitor.composeapp.generated.resources.live_event_power_updated
import ficsitmonitor.composeapp.generated.resources.live_event_production_updated
import ficsitmonitor.composeapp.generated.resources.live_event_server_status_format
import ficsitmonitor.composeapp.generated.resources.live_event_sink_updated
import ficsitmonitor.composeapp.generated.resources.live_event_title_drones
import ficsitmonitor.composeapp.generated.resources.live_event_title_extractors
import ficsitmonitor.composeapp.generated.resources.live_event_title_factory
import ficsitmonitor.composeapp.generated.resources.live_event_title_generators
import ficsitmonitor.composeapp.generated.resources.live_event_title_inventory
import ficsitmonitor.composeapp.generated.resources.live_event_title_metrics
import ficsitmonitor.composeapp.generated.resources.live_event_title_players
import ficsitmonitor.composeapp.generated.resources.live_event_title_power
import ficsitmonitor.composeapp.generated.resources.live_event_title_production
import ficsitmonitor.composeapp.generated.resources.live_event_title_server
import ficsitmonitor.composeapp.generated.resources.live_event_title_sink
import ficsitmonitor.composeapp.generated.resources.live_event_title_trains
import ficsitmonitor.composeapp.generated.resources.live_event_trains_derailed_format
import ficsitmonitor.composeapp.generated.resources.live_event_trains_updated
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventCard(
    event: LiveEvent,
    modifier: Modifier = Modifier,
) {
    val icon = eventIcon(event.icon)
    val title = eventTitle(event.kind)
    val subtitle = eventSubtitle(event.kind)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(FicsitTheme.colors.bgCard)
            .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FicsitTheme.colors.accentCyan,
            modifier = Modifier.size(20.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = FicsitTheme.colors.textSecondary,
            )
        }
        Text(
            text = event.timestamp,
            style = MaterialTheme.typography.labelSmall,
            color = FicsitTheme.colors.textMuted,
        )
    }
}

@Composable
private fun eventTitle(kind: LiveWsEvent): String = when (kind) {
    is LiveWsEvent.PowerUpdated, is LiveWsEvent.FuseTriggered ->
        stringResource(Res.string.live_event_title_power)

    is LiveWsEvent.FactoryUpdated -> stringResource(Res.string.live_event_title_factory)
    is LiveWsEvent.TrainsUpdated, is LiveWsEvent.TrainsDerailed ->
        stringResource(Res.string.live_event_title_trains)

    is LiveWsEvent.DronesUpdated -> stringResource(Res.string.live_event_title_drones)
    is LiveWsEvent.PlayersOnline -> stringResource(Res.string.live_event_title_players)
    is LiveWsEvent.ProductionUpdated -> stringResource(Res.string.live_event_title_production)
    is LiveWsEvent.GeneratorsUpdated -> stringResource(Res.string.live_event_title_generators)
    is LiveWsEvent.ExtractorsUpdated -> stringResource(Res.string.live_event_title_extractors)
    is LiveWsEvent.ServerStatus -> stringResource(Res.string.live_event_title_server)
    is LiveWsEvent.MetricsUpdated -> stringResource(Res.string.live_event_title_metrics)
    is LiveWsEvent.InventoryUpdated -> stringResource(Res.string.live_event_title_inventory)
    is LiveWsEvent.SinkUpdated -> stringResource(Res.string.live_event_title_sink)
}

@Composable
private fun eventSubtitle(kind: LiveWsEvent): String = when (kind) {
    is LiveWsEvent.ServerStatus -> stringResource(Res.string.live_event_server_status_format, kind.status)
    is LiveWsEvent.MetricsUpdated -> stringResource(Res.string.live_event_metrics_updated)
    is LiveWsEvent.PowerUpdated -> stringResource(Res.string.live_event_power_updated)
    is LiveWsEvent.FuseTriggered -> stringResource(Res.string.live_event_fuse_triggered_format, kind.circuits)
    is LiveWsEvent.ProductionUpdated -> stringResource(Res.string.live_event_production_updated)
    is LiveWsEvent.PlayersOnline -> stringResource(Res.string.live_event_players_online_format, kind.online)
    is LiveWsEvent.TrainsUpdated -> stringResource(Res.string.live_event_trains_updated)
    is LiveWsEvent.TrainsDerailed -> stringResource(Res.string.live_event_trains_derailed_format, kind.count)
    is LiveWsEvent.DronesUpdated -> stringResource(Res.string.live_event_drones_updated)
    is LiveWsEvent.GeneratorsUpdated -> stringResource(Res.string.live_event_generators_updated)
    is LiveWsEvent.FactoryUpdated -> stringResource(Res.string.live_event_factory_updated)
    is LiveWsEvent.ExtractorsUpdated -> stringResource(Res.string.live_event_extractors_updated)
    is LiveWsEvent.InventoryUpdated -> stringResource(Res.string.live_event_inventory_updated)
    is LiveWsEvent.SinkUpdated -> stringResource(Res.string.live_event_sink_updated)
}

private fun eventIcon(iconName: String): ImageVector = when (iconName) {
    "bolt" -> Icons.Default.Bolt
    "train" -> Icons.Default.Train
    "warning" -> Icons.Default.Warning
    "groups" -> Icons.Default.Groups
    "factory" -> Icons.Default.PrecisionManufacturing
    else -> Icons.Default.Notifications
}
