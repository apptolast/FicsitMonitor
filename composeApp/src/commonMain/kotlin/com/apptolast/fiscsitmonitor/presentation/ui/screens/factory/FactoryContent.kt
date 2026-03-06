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
import com.apptolast.fiscsitmonitor.presentation.ui.components.ExtractorCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.InventoryItem
import com.apptolast.fiscsitmonitor.presentation.ui.components.MachineCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.MetricCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.badge_every_30s
import ficsitmonitor.composeapp.generated.resources.badge_every_60s
import ficsitmonitor.composeapp.generated.resources.label_active
import ficsitmonitor.composeapp.generated.resources.label_buildings
import ficsitmonitor.composeapp.generated.resources.label_paused
import ficsitmonitor.composeapp.generated.resources.label_stopped
import ficsitmonitor.composeapp.generated.resources.section_extractors
import ficsitmonitor.composeapp.generated.resources.section_factory_status
import ficsitmonitor.composeapp.generated.resources.section_world_inventory
import org.jetbrains.compose.resources.stringResource

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
            title = stringResource(Res.string.section_factory_status),
            icon = Icons.Default.PrecisionManufacturing,
            iconTint = FicsitTheme.colors.accentCyan,
            badgeText = stringResource(Res.string.badge_every_60s),
        )

        // Summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MetricCard(stringResource(Res.string.label_buildings), "${buildings.size}", "", Icons.Default.PrecisionManufacturing, modifier = Modifier.weight(1f))
            MetricCard(stringResource(Res.string.label_active), "$active", "", Icons.Default.PrecisionManufacturing, iconTint = FicsitTheme.colors.accentGreen, modifier = Modifier.weight(1f))
            MetricCard(stringResource(Res.string.label_stopped), "$stopped", "", Icons.Default.PrecisionManufacturing, iconTint = FicsitTheme.colors.accentRed, modifier = Modifier.weight(1f))
            MetricCard(stringResource(Res.string.label_paused), "$paused", "", Icons.Default.PrecisionManufacturing, iconTint = FicsitTheme.colors.accentOrange, modifier = Modifier.weight(1f))
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
    }
}
