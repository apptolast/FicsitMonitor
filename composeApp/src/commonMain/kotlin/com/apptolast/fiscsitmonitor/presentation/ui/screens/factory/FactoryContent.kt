package com.apptolast.fiscsitmonitor.presentation.ui.screens.factory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.data.model.WorldInventoryItemDto
import com.apptolast.fiscsitmonitor.presentation.ui.components.BadgeType
import com.apptolast.fiscsitmonitor.presentation.ui.components.ExtractorCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.InventoryItem
import com.apptolast.fiscsitmonitor.presentation.ui.components.MachineCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.MetricCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme

@Composable
fun FactoryContent(
    buildings: List<FactoryBuildingDto>,
    extractors: List<ExtractorDto>,
    worldInventory: List<WorldInventoryItemDto>,
    modifier: Modifier = Modifier,
) {
    val active = buildings.count { it.isProducing }
    val stopped = buildings.count { !it.isProducing && !it.isPaused }
    val paused = buildings.count { it.isPaused }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SectionHeader(
            title = "FACTORY STATUS",
            icon = Icons.Default.PrecisionManufacturing,
            iconTint = FicsitTheme.colors.accentCyan,
            badgeText = "EVERY 60S",
        )

        // Summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MetricCard("BUILDINGS", "${buildings.size}", "", Icons.Default.PrecisionManufacturing, modifier = Modifier.weight(1f))
            MetricCard("ACTIVE", "$active", "", Icons.Default.PrecisionManufacturing, iconTint = FicsitTheme.colors.accentGreen, modifier = Modifier.weight(1f))
            MetricCard("STOPPED", "$stopped", "", Icons.Default.PrecisionManufacturing, iconTint = FicsitTheme.colors.accentRed, modifier = Modifier.weight(1f))
            MetricCard("PAUSED", "$paused", "", Icons.Default.PrecisionManufacturing, iconTint = FicsitTheme.colors.accentOrange, modifier = Modifier.weight(1f))
        }

        // Machine list
        buildings.forEach { building ->
            MachineCard(building = building)
        }

        // Extractors
        if (extractors.isNotEmpty()) {
            SectionHeader(
                title = "EXTRACTORS (${extractors.size})",
                icon = Icons.Default.Hardware,
                iconTint = FicsitTheme.colors.accentOrange,
                badgeText = "EVERY 30S",
            )
            extractors.forEach { extractor ->
                ExtractorCard(extractor = extractor)
            }
        }

        // World Inventory
        if (worldInventory.isNotEmpty()) {
            SectionHeader(
                title = "WORLD INVENTORY",
                icon = Icons.Default.Inventory2,
                badgeText = "EVERY 30S",
            )
            worldInventory.forEach { item ->
                InventoryItem(item = item)
            }
        }
    }
}
