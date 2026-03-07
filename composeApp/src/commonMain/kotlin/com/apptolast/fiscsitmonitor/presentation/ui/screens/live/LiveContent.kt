package com.apptolast.fiscsitmonitor.presentation.ui.screens.live

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.presentation.ui.components.BadgeType
import com.apptolast.fiscsitmonitor.presentation.ui.components.EmptyState
import com.apptolast.fiscsitmonitor.presentation.ui.components.EventCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.components.SummaryRow
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveSummary
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.badge_real_time
import ficsitmonitor.composeapp.generated.resources.empty_events
import ficsitmonitor.composeapp.generated.resources.section_live_events
import ficsitmonitor.composeapp.generated.resources.section_summary
import ficsitmonitor.composeapp.generated.resources.summary_active_circuits
import ficsitmonitor.composeapp.generated.resources.summary_active_trains
import ficsitmonitor.composeapp.generated.resources.summary_fuses_triggered
import ficsitmonitor.composeapp.generated.resources.summary_items_in_production
import ficsitmonitor.composeapp.generated.resources.summary_players_online
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LiveContent(
    events: List<LiveEvent>,
    summary: LiveSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Summary
        SectionHeader(
            title = stringResource(Res.string.section_summary),
            icon = Icons.Default.Notifications,
            iconTint = FicsitTheme.colors.accentPurple,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(FicsitTheme.colors.bgCard)
                .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
                .padding(16.dp),
        ) {
            SummaryRow(
                label = stringResource(Res.string.summary_players_online),
                value = "${summary.playersOnline}",
                valueColor = FicsitTheme.colors.accentGreen,
            )
            SummaryRow(
                label = stringResource(Res.string.summary_active_circuits),
                value = "${summary.activeCircuits}",
                valueColor = FicsitTheme.colors.accentCyan,
            )
            SummaryRow(
                label = stringResource(Res.string.summary_active_trains),
                value = "${summary.activeTrains}",
                valueColor = FicsitTheme.colors.accentOrange,
            )
            SummaryRow(
                label = stringResource(Res.string.summary_items_in_production),
                value = "${summary.itemsInProduction}",
            )
            SummaryRow(
                label = stringResource(Res.string.summary_fuses_triggered),
                value = "${summary.fusesTriggered}",
                valueColor = if (summary.fusesTriggered > 0) FicsitTheme.colors.accentRed else MaterialTheme.colorScheme.onBackground,
                showDivider = false,
            )
        }

        // Activity feed
        SectionHeader(
            title = stringResource(Res.string.section_live_events),
            icon = Icons.Default.Notifications,
            iconTint = FicsitTheme.colors.accentCyan,
            badgeText = stringResource(Res.string.badge_real_time),
            badgeType = BadgeType.INFO,
        )

        if (events.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(FicsitTheme.colors.bgCard)
                    .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
                    .padding(16.dp),
            ) {
                EmptyState(
                    icon = Icons.Default.SentimentDissatisfied,
                    message = stringResource(Res.string.empty_events),
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                events.forEach { event ->
                    EventCard(event = event)
                }
            }
        }
    }
}

@Preview
@Composable
private fun LiveContentPreview() {
    FicsitMonitorTheme {
        LiveContent(
            events = listOf(
                LiveEvent(
                    icon = "\u26A1",
                    title = "Power",
                    subtitle = "Fuse triggered (1 circuits)",
                    timestamp = "+00:01:23"
                ),
                LiveEvent(
                    icon = "\uD83C\uDFED",
                    title = "Factory",
                    subtitle = "Factory buildings updated",
                    timestamp = "+00:01:20"
                ),
                LiveEvent(
                    icon = "\uD83D\uDE82",
                    title = "Trains",
                    subtitle = "Trains updated",
                    timestamp = "+00:01:15"
                ),
                LiveEvent(
                    icon = "\uD83D\uDC64",
                    title = "Players",
                    subtitle = "2 players online",
                    timestamp = "+00:01:10"
                ),
            ),
            summary = LiveSummary(
                playersOnline = 2,
                activeCircuits = 1,
                activeTrains = 3,
                itemsInProduction = 15,
                fusesTriggered = 1,
            ),
        )
    }
}
