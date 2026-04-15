package com.apptolast.fiscsitmonitor.presentation.ui.screens.factory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.BuildingProductionDto
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.ResourceSinkDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import com.apptolast.fiscsitmonitor.presentation.ui.components.ExtractorCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.InventoryItem
import com.apptolast.fiscsitmonitor.presentation.ui.components.MachineCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.MetricCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatPercent
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.badge_every_30s
import ficsitmonitor.composeapp.generated.resources.badge_every_60s
import ficsitmonitor.composeapp.generated.resources.label_active
import ficsitmonitor.composeapp.generated.resources.label_buildings
import ficsitmonitor.composeapp.generated.resources.label_coupons
import ficsitmonitor.composeapp.generated.resources.label_next_coupon
import ficsitmonitor.composeapp.generated.resources.label_paused
import ficsitmonitor.composeapp.generated.resources.label_stopped
import ficsitmonitor.composeapp.generated.resources.label_total_points
import ficsitmonitor.composeapp.generated.resources.section_extractors
import ficsitmonitor.composeapp.generated.resources.section_factory_status
import ficsitmonitor.composeapp.generated.resources.section_resource_sink
import ficsitmonitor.composeapp.generated.resources.section_world_inventory
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FactoryContent(
    buildings: List<FactoryBuildingDto>,
    extractors: List<ExtractorDto>,
    worldInventory: List<WorldInventoryItemDto>,
    resourceSink: ResourceSinkDto?,
    modifier: Modifier = Modifier,
) {
    val active = buildings.count { it.isProducing }
    val stopped = buildings.count { !it.isProducing && !it.isPaused }
    val paused = buildings.count { it.isPaused }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SectionHeader(
            title = stringResource(Res.string.section_factory_status),
            icon = Icons.Default.PrecisionManufacturing,
            iconTint = FicsitTheme.colors.accentCyan,
            badgeText = stringResource(Res.string.badge_every_60s),
        )

        // Summary 2x2 grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    stringResource(Res.string.label_buildings),
                    "${buildings.size}",
                    "",
                    Icons.Default.PrecisionManufacturing,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    stringResource(Res.string.label_active),
                    "$active",
                    "",
                    Icons.Default.PrecisionManufacturing,
                    iconTint = FicsitTheme.colors.accentGreen,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    stringResource(Res.string.label_stopped),
                    "$stopped",
                    "",
                    Icons.Default.PrecisionManufacturing,
                    iconTint = FicsitTheme.colors.accentRed,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    stringResource(Res.string.label_paused),
                    "$paused",
                    "",
                    Icons.Default.PrecisionManufacturing,
                    iconTint = FicsitTheme.colors.accentOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Machine list
        buildings.forEach { building ->
            MachineCard(building = building)
        }

        // Extractors
        if (extractors.isNotEmpty()) {
            SectionHeader(
                title = stringResource(Res.string.section_extractors, extractors.size),
                icon = Icons.Default.Hardware,
                iconTint = FicsitTheme.colors.accentOrange,
                badgeText = stringResource(Res.string.badge_every_30s),
            )
            extractors.forEach { extractor ->
                ExtractorCard(extractor = extractor)
            }
        }

        // World Inventory
        if (worldInventory.isNotEmpty()) {
            SectionHeader(
                title = stringResource(Res.string.section_world_inventory),
                icon = Icons.Default.Inventory2,
                badgeText = stringResource(Res.string.badge_every_30s),
            )
            worldInventory.forEach { item ->
                InventoryItem(item = item)
            }
        }

        // Resource Sink (AWESOME)
        if (resourceSink != null) {
            SectionHeader(
                title = stringResource(Res.string.section_resource_sink),
                icon = Icons.Default.Star,
                iconTint = FicsitTheme.colors.accentPurple,
                badgeText = stringResource(Res.string.badge_every_60s),
            )

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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MetricCard(
                        label = stringResource(Res.string.label_coupons),
                        value = "${resourceSink.numCoupon}",
                        subtitle = "",
                        icon = Icons.Default.Star,
                        iconTint = FicsitTheme.colors.accentPurple,
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = stringResource(Res.string.label_total_points),
                        value = formatPoints(resourceSink.totalPoints),
                        subtitle = "",
                        icon = Icons.Default.Star,
                        iconTint = FicsitTheme.colors.accentYellow,
                        modifier = Modifier.weight(1f),
                    )
                }

                // Progress to next coupon
                Text(
                    text = stringResource(Res.string.label_next_coupon) + " ${resourceSink.percent.formatPercent()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = FicsitTheme.colors.textMuted,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(FicsitTheme.colors.border),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = (resourceSink.percent / 100.0).toFloat().coerceIn(0f, 1f))
                            .clip(RoundedCornerShape(3.dp))
                            .background(FicsitTheme.colors.accentPurple),
                    )
                }
            }
        }
    }
}

private fun formatPoints(points: Long): String = when {
    points >= 1_000_000 -> "${(points / 100_000) / 10.0}M"
    points >= 1_000 -> "${(points / 100) / 10.0}K"
    else -> "$points"
}

@Preview
@Composable
private fun FactoryContentPreview() {
    FicsitMonitorTheme {
        FactoryContent(
            buildings = listOf(
                FactoryBuildingDto(
                    id = "b1",
                    name = "Smelter",
                    recipe = "Iron Ingot",
                    isProducing = true,
                    isConfigured = true,
                    manuSpeed = 100.0,
                    powerConsumed = 4.0,
                    production = listOf(
                        BuildingProductionDto(
                            name = "Iron Ingot",
                            currentProd = 30.0,
                            maxProd = 30.0,
                            prodPercent = 100.0
                        ),
                    ),
                ),
                FactoryBuildingDto(
                    id = "b2",
                    name = "Constructor",
                    recipe = "Iron Plate",
                    isProducing = true,
                    isConfigured = true,
                    manuSpeed = 100.0,
                    powerConsumed = 4.0,
                    production = listOf(
                        BuildingProductionDto(
                            name = "Iron Plate",
                            currentProd = 20.0,
                            maxProd = 20.0,
                            prodPercent = 100.0
                        ),
                    ),
                ),
                FactoryBuildingDto(
                    id = "b3",
                    name = "Assembler",
                    recipe = "Reinforced Iron Plate",
                    isPaused = true,
                    isConfigured = true,
                ),
            ),
            extractors = listOf(
                ExtractorDto(
                    id = "ext1",
                    name = "Miner Mk.2",
                    isProducing = true,
                    isConfigured = true,
                    prodName = "Iron Ore",
                    prodCurrent = 120.0,
                    prodMax = 120.0,
                    prodPercent = 100.0,
                    powerConsumed = 12.0,
                ),
            ),
            worldInventory = listOf(
                WorldInventoryItemDto(name = "Iron Ore", amount = 5400, maxAmount = 10000),
                WorldInventoryItemDto(name = "Copper Ore", amount = 2300, maxAmount = 10000),
            ),
            resourceSink = ResourceSinkDto(
                name = "AWESOME Sink",
                totalPoints = 1_250_000,
                pointsToCoupon = 2_000_000,
                numCoupon = 3,
                percent = 62.5,
            ),
        )
    }
}
