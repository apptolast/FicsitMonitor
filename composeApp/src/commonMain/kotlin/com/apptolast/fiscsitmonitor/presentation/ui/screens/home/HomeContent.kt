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
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PlayerDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.data.model.ProductionItemDto
import com.apptolast.fiscsitmonitor.data.model.ServerDto
import com.apptolast.fiscsitmonitor.data.model.ServerMetricsDto
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.presentation.ui.components.AlertBanner
import com.apptolast.fiscsitmonitor.presentation.ui.components.BadgeType
import com.apptolast.fiscsitmonitor.presentation.ui.components.FicsitStatusBar
import com.apptolast.fiscsitmonitor.presentation.ui.components.InfoRow
import com.apptolast.fiscsitmonitor.presentation.ui.components.MetricCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.NativeAd
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatDuration
import com.apptolast.fiscsitmonitor.util.formatMW
import com.apptolast.fiscsitmonitor.util.formatTPS
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.alert_fuse_triggered
import ficsitmonitor.composeapp.generated.resources.badge_live
import ficsitmonitor.composeapp.generated.resources.current_per_min_format
import ficsitmonitor.composeapp.generated.resources.derailed_format
import ficsitmonitor.composeapp.generated.resources.fuse_triggered_format
import ficsitmonitor.composeapp.generated.resources.items_format
import ficsitmonitor.composeapp.generated.resources.label_circuits
import ficsitmonitor.composeapp.generated.resources.label_energy
import ficsitmonitor.composeapp.generated.resources.label_extractors
import ficsitmonitor.composeapp.generated.resources.label_generators
import ficsitmonitor.composeapp.generated.resources.label_phase
import ficsitmonitor.composeapp.generated.resources.label_players
import ficsitmonitor.composeapp.generated.resources.label_production
import ficsitmonitor.composeapp.generated.resources.label_session
import ficsitmonitor.composeapp.generated.resources.label_tech_tier
import ficsitmonitor.composeapp.generated.resources.label_tick_rate
import ficsitmonitor.composeapp.generated.resources.label_trains
import ficsitmonitor.composeapp.generated.resources.max_per_min_format
import ficsitmonitor.composeapp.generated.resources.mw_cap_format
import ficsitmonitor.composeapp.generated.resources.no_data
import ficsitmonitor.composeapp.generated.resources.of_max_format
import ficsitmonitor.composeapp.generated.resources.players_format
import ficsitmonitor.composeapp.generated.resources.producing_format
import ficsitmonitor.composeapp.generated.resources.satisfactory_server
import ficsitmonitor.composeapp.generated.resources.section_key_metrics
import ficsitmonitor.composeapp.generated.resources.subtitle_no_energy
import ficsitmonitor.composeapp.generated.resources.subtitle_peak_demand
import ficsitmonitor.composeapp.generated.resources.subtitle_running
import ficsitmonitor.composeapp.generated.resources.subtitle_server_idle
import ficsitmonitor.composeapp.generated.resources.tier_format
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeContent(
    server: ServerDto?,
    metrics: ServerMetricsDto?,
    players: List<PlayerDto>,
    circuits: List<PowerCircuitDto>,
    production: List<ProductionItemDto>,
    extractors: List<ExtractorDto>,
    generators: List<GeneratorDto>,
    trains: List<TrainDto>,
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
) {
    val fuseTriggered = circuits.any { it.fuseTriggered }
    val noData = stringResource(Res.string.no_data)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Header
        FicsitStatusBar(
            isOnline = server?.status == "online",
            serverAddress = "${server?.host ?: "..."} \u00B7 ${server?.name ?: stringResource(Res.string.satisfactory_server)}",
            onOpenSettings = onOpenSettings,
        )

        // Alert
        if (fuseTriggered) {
            AlertBanner(message = stringResource(Res.string.alert_fuse_triggered))
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
            InfoRow(
                stringResource(Res.string.label_tech_tier),
                stringResource(Res.string.tier_format, metrics?.techTier ?: 0)
            )
            InfoRow(stringResource(Res.string.label_phase), metrics?.gamePhase ?: noData)
            InfoRow(stringResource(Res.string.label_tick_rate), metrics?.tickRate?.formatTPS() ?: "0.0 TPS")
            InfoRow(
                stringResource(Res.string.label_players),
                stringResource(Res.string.players_format, metrics?.playerCount ?: 0)
            )
            InfoRow(stringResource(Res.string.label_session), metrics?.totalDuration?.formatDuration() ?: "0h 00m")
        }

        // Metrics Grid
        SectionHeader(
            title = stringResource(Res.string.section_key_metrics),
            icon = Icons.Default.Analytics,
            badgeText = stringResource(Res.string.badge_live),
            badgeType = BadgeType.SUCCESS,
        )

        NativeAd()

        val totalConsumed = circuits.sumOf { it.powerConsumed }
        val totalCapacity = circuits.sumOf { it.powerCapacity }
        val fusesCount = circuits.count { it.fuseTriggered }
        val onlinePlayers = players.count { it.isOnline }
        val producingItems = production.count { it.currentProd > 0 }
        val currentProd = production.sumOf { it.currentProd } ?: 0.0
        val maxProd = production.maxOfOrNull { it.maxProd } ?: 0.0
        val extractorsProducing = extractors.count { it.isProducing }
        val totalGeneratorCapacity = generators.sumOf { it.capacityMw }
        val derailedTrains = trains.count { it.isDerailed }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    label = stringResource(Res.string.label_energy),
                    value = "${totalConsumed.formatMW()} / ${totalCapacity.formatMW()}",
                    subtitle = stringResource(Res.string.subtitle_peak_demand),
                    icon = Icons.Default.Bolt,
                    iconTint = FicsitTheme.colors.accentYellow,
                )
                MetricCard(
                    label = stringResource(Res.string.label_production),
                    value = if (producingItems > 0) stringResource(
                        Res.string.items_format,
                        producingItems
                    ) else stringResource(Res.string.subtitle_no_energy),
                    subtitle = stringResource(Res.string.current_per_min_format, currentProd.toInt()) + "\n" +
                            stringResource(Res.string.max_per_min_format, maxProd.toInt()),
                    icon = Icons.Default.PrecisionManufacturing,
                    iconTint = FicsitTheme.colors.accentCyan,
                )
                MetricCard(
                    label = stringResource(Res.string.label_tick_rate),
                    value = metrics?.tickRate?.formatTPS() ?: "0.0 TPS",
                    subtitle = if (metrics?.playerCount == 0) stringResource(Res.string.subtitle_server_idle) else stringResource(
                        Res.string.subtitle_running
                    ),
                    icon = Icons.Default.Speed,
                )
                MetricCard(
                    label = stringResource(Res.string.label_extractors),
                    value = "${extractors.size}",
                    subtitle = stringResource(Res.string.producing_format, extractorsProducing),
                    icon = Icons.Default.Hardware,
                    iconTint = FicsitTheme.colors.accentOrange,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    label = stringResource(Res.string.label_circuits),
                    value = "${circuits.size}",
                    subtitle = stringResource(Res.string.fuse_triggered_format, fusesCount),
                    icon = Icons.Default.Bolt,
                    iconTint = FicsitTheme.colors.accentRed,
                )
                MetricCard(
                    label = stringResource(Res.string.label_players),
                    value = "$onlinePlayers",
                    subtitle = stringResource(Res.string.of_max_format),
                    icon = Icons.Default.Group,
                    iconTint = FicsitTheme.colors.accentPurple,
                )
                MetricCard(
                    label = stringResource(Res.string.label_trains),
                    value = "${trains.size}",
                    subtitle = stringResource(Res.string.derailed_format, derailedTrains),
                    icon = Icons.Default.LocalShipping,
                    iconTint = FicsitTheme.colors.accentCyan,
                )
                MetricCard(
                    label = stringResource(Res.string.label_generators),
                    value = "${generators.size}",
                    subtitle = stringResource(Res.string.mw_cap_format, totalGeneratorCapacity.formatMW()),
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = FicsitTheme.colors.accentOrange,
                )
            }
        }

    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    FicsitMonitorTheme {
        HomeContent(
            server = ServerDto(
                id = 1,
                name = "My Factory",
                host = "192.168.1.100",
                apiPort = 8080,
                frmHttpPort = 8081,
                status = "online",
            ),
            metrics = ServerMetricsDto(
                tickRate = 29.8,
                playerCount = 2,
                techTier = 7,
                gamePhase = "Space Elevator Phase 3",
                isRunning = true,
                totalDuration = 345600,
            ),
            players = listOf(
                PlayerDto(id = 1, name = "Pioneer_1", isOnline = true, health = 100.0),
                PlayerDto(id = 2, name = "Pioneer_2", isOnline = false, health = 75.0),
            ),
            circuits = listOf(
                PowerCircuitDto(
                    circuitGroupId = 0,
                    powerProduction = 450.0,
                    powerConsumed = 380.5,
                    powerCapacity = 500.0,
                    powerMaxConsumed = 420.0,
                    fuseTriggered = false,
                ),
            ),
            production = listOf(
                ProductionItemDto(itemName = "Iron Plate", currentProd = 120.0, maxProd = 735.0, prodPercent = 16.3),
                ProductionItemDto(itemName = "Iron Rod", currentProd = 60.0, maxProd = 400.0, prodPercent = 15.0),
            ),
            extractors = listOf(
                ExtractorDto(id = "ext1", name = "Miner Mk.2", isProducing = true, prodName = "Iron Ore"),
                ExtractorDto(id = "ext2", name = "Miner Mk.1", isProducing = true, prodName = "Copper Ore"),
                ExtractorDto(id = "ext3", name = "Oil Extractor", isProducing = false, prodName = "Crude Oil"),
            ),
            generators = listOf(
                GeneratorDto(id = "gen1", name = "Coal Generator", capacityMw = 75.0, loadPct = 80.0),
                GeneratorDto(id = "gen2", name = "Coal Generator", capacityMw = 75.0, loadPct = 65.0),
            ),
            trains = listOf(
                TrainDto(frmId = "train1", name = "Iron Express", selfDriving = true, currentStation = "Iron Depot"),
                TrainDto(frmId = "train2", name = "Coal Runner", selfDriving = true, isDerailed = true),
            ),
        )
    }
}
