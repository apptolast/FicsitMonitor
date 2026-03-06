package com.apptolast.fiscsitmonitor.presentation.ui.screens.home

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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.presentation.ui.components.AlertBanner
import com.apptolast.fiscsitmonitor.presentation.ui.components.BadgeType
import com.apptolast.fiscsitmonitor.presentation.ui.components.FicsitStatusBar
import com.apptolast.fiscsitmonitor.presentation.ui.components.InfoRow
import com.apptolast.fiscsitmonitor.presentation.ui.components.MetricCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatDuration
import com.apptolast.fiscsitmonitor.util.formatMW
import com.apptolast.fiscsitmonitor.util.formatTPS

@Composable
fun HomeContent(
    server: ServerDto?,
    metrics: ServerMetricsDto?,
    players: List<PlayerDto>,
    circuits: List<PowerCircuitDto>,
    production: List<ProductionItemDto>,
    modifier: Modifier = Modifier,
) {
    val fuseTriggered = circuits.any { it.fuseTriggered }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Header
        FicsitStatusBar(
            isOnline = server?.status == "online",
            serverAddress = "${server?.host ?: "..."} \u00B7 ${server?.name ?: "Satisfactory Server"}",
        )

        // Alert
        if (fuseTriggered) {
            AlertBanner(message = "FUSE TRIGGERED \u2014 Power grid overload")
        }

        // Server Info Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(FicsitTheme.colors.bgCard)
                .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp),
        ) {
            InfoRow("TECH TIER", "Tier ${metrics?.techTier ?: 0}")
            InfoRow("PHASE", metrics?.gamePhase ?: "---")
            InfoRow("TICK RATE", metrics?.tickRate?.formatTPS() ?: "0.0 TPS")
            InfoRow("PLAYERS", "${metrics?.playerCount ?: 0} / 8")
            InfoRow("SESSION", metrics?.totalDuration?.formatDuration() ?: "0h 00m")
        }

        // Metrics Grid
        SectionHeader(
            title = "KEY METRICS",
            icon = Icons.Default.Analytics,
            badgeText = "LIVE",
            badgeType = BadgeType.SUCCESS,
        )

        val totalConsumed = circuits.sumOf { it.powerConsumed }
        val totalProduction = circuits.sumOf { it.powerMaxConsumed }
        val fusesCount = circuits.count { it.fuseTriggered }
        val onlinePlayers = players.count { it.isOnline }
        val producingItems = production.count { it.currentProd > 0 }
        val maxProd = production.maxOfOrNull { it.maxProd } ?: 0.0

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    label = "ENERGY",
                    value = totalConsumed.formatMW(),
                    subtitle = "Peak demand",
                    icon = Icons.Default.Bolt,
                    iconTint = FicsitTheme.colors.accentYellow,
                )
                MetricCard(
                    label = "PRODUCTION",
                    value = if (producingItems > 0) "$producingItems items" else "No energy",
                    subtitle = "Max: ${maxProd.toInt()}/min",
                    icon = Icons.Default.PrecisionManufacturing,
                    iconTint = FicsitTheme.colors.accentCyan,
                )
                MetricCard(
                    label = "TICK RATE",
                    value = metrics?.tickRate?.formatTPS() ?: "0.0 TPS",
                    subtitle = if (metrics?.playerCount == 0) "Server idle" else "Running",
                    icon = Icons.Default.Speed,
                )
                MetricCard(
                    label = "EXTRACTORS",
                    value = "---",
                    subtitle = "0 producing",
                    icon = Icons.Default.Hardware,
                    iconTint = FicsitTheme.colors.accentOrange,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    label = "CIRCUITS",
                    value = "${circuits.size}",
                    subtitle = "$fusesCount fuse triggered",
                    icon = Icons.Default.Bolt,
                    iconTint = FicsitTheme.colors.accentRed,
                )
                MetricCard(
                    label = "PLAYERS",
                    value = "$onlinePlayers",
                    subtitle = "of 8 max",
                    icon = Icons.Default.Group,
                    iconTint = FicsitTheme.colors.accentPurple,
                )
                MetricCard(
                    label = "TRAINS",
                    value = "0",
                    subtitle = "0 derailed",
                    icon = Icons.Default.LocalShipping,
                    iconTint = FicsitTheme.colors.accentCyan,
                )
                MetricCard(
                    label = "GENERATORS",
                    value = "---",
                    subtitle = "--- MW cap.",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = FicsitTheme.colors.accentOrange,
                )
            }
        }
    }
}
