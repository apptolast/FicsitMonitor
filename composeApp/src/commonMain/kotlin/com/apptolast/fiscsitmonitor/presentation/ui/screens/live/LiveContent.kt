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
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveSummary

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
        SectionHeader(
            title = "LIVE EVENTS",
            icon = Icons.Default.Notifications,
            iconTint = FicsitTheme.colors.accentCyan,
            badgeText = "REAL-TIME",
            badgeType = BadgeType.INFO,
        )

        // Activity feed
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
                    message = "No events yet",
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                events.forEach { event ->
                    EventCard(event = event)
                }
            }
        }

        // Summary
        SectionHeader(
            title = "SUMMARY",
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
                label = "Players Online",
                value = "${summary.playersOnline}",
                valueColor = FicsitTheme.colors.accentGreen,
            )
            SummaryRow(
                label = "Active Circuits",
                value = "${summary.activeCircuits}",
                valueColor = FicsitTheme.colors.accentCyan,
            )
            SummaryRow(
                label = "Active Trains",
                value = "${summary.activeTrains}",
                valueColor = FicsitTheme.colors.accentOrange,
            )
            SummaryRow(
                label = "Items in Production",
                value = "${summary.itemsInProduction}",
            )
            SummaryRow(
                label = "Fuses Triggered",
                value = "${summary.fusesTriggered}",
                valueColor = if (summary.fusesTriggered > 0) FicsitTheme.colors.accentRed else MaterialTheme.colorScheme.onBackground,
                showDivider = false,
            )
        }
    }
}
