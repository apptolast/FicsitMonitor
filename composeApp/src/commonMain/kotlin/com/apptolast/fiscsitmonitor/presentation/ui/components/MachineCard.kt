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
import com.apptolast.fiscsitmonitor.data.model.FactoryBuildingDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatMW
import com.apptolast.fiscsitmonitor.util.formatPercent
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.fallback_building
import ficsitmonitor.composeapp.generated.resources.label_active
import ficsitmonitor.composeapp.generated.resources.label_paused
import ficsitmonitor.composeapp.generated.resources.label_power
import ficsitmonitor.composeapp.generated.resources.label_recipe
import ficsitmonitor.composeapp.generated.resources.label_speed
import ficsitmonitor.composeapp.generated.resources.label_stopped
import ficsitmonitor.composeapp.generated.resources.no_data
import org.jetbrains.compose.resources.stringResource

@Composable
fun MachineCard(
    building: FactoryBuildingDto,
    modifier: Modifier = Modifier,
) {
    val statusText = when {
        building.isPaused -> stringResource(Res.string.label_paused)
        building.isProducing -> stringResource(Res.string.label_active)
        else -> stringResource(Res.string.label_stopped)
    }
    val statusType = when {
        building.isPaused -> BadgeType.WARNING
        building.isProducing -> BadgeType.SUCCESS
        else -> BadgeType.ERROR
    }
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
            Text(
                text = building.name.ifEmpty { stringResource(Res.string.fallback_building) },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            StatusBadge(text = statusText, type = statusType)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MachineDetail(stringResource(Res.string.label_recipe), building.recipe.ifEmpty { noData })
            MachineDetail(stringResource(Res.string.label_speed), (building.currentPotential * 100).formatPercent())
            MachineDetail(stringResource(Res.string.label_power), building.powerConsumption.formatMW())
        }
    }
}

@Composable
private fun MachineDetail(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
    }
}
