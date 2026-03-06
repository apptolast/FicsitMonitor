package com.apptolast.fiscsitmonitor.presentation.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatMW
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.badge_fuse
import ficsitmonitor.composeapp.generated.resources.circuit_format
import ficsitmonitor.composeapp.generated.resources.label_consumed
import ficsitmonitor.composeapp.generated.resources.label_capacity
import ficsitmonitor.composeapp.generated.resources.label_peak
import ficsitmonitor.composeapp.generated.resources.label_prod
import ficsitmonitor.composeapp.generated.resources.label_use
import org.jetbrains.compose.resources.stringResource

@Composable
fun CircuitCard(
    circuit: PowerCircuitDto,
    modifier: Modifier = Modifier,
) {
    val usagePercent = if (circuit.powerCapacity > 0) {
        (circuit.powerConsumed / circuit.powerCapacity * 100).coerceIn(0.0, 100.0)
    } else 0.0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(FicsitTheme.colors.bgCard)
            .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.circuit_format, circuit.circuitGroupId),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (circuit.fuseTriggered) {
                StatusBadge(text = stringResource(Res.string.badge_fuse), type = BadgeType.ERROR)
            }
        }

        // Metrics row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MetricColumn(stringResource(Res.string.label_consumed), circuit.powerConsumed.formatMW())
            MetricColumn(stringResource(Res.string.label_capacity), circuit.powerCapacity.formatMW())
            MetricColumn(stringResource(Res.string.label_prod), circuit.powerProduction.formatMW())
        }

        // Peak
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(Res.string.label_peak), style = MaterialTheme.typography.labelLarge, color = FicsitTheme.colors.textSecondary)
            Text(circuit.powerMaxConsumed.formatMW(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
        }

        // Usage bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(Res.string.label_use), style = MaterialTheme.typography.labelLarge, color = FicsitTheme.colors.textSecondary)
            Text("${usagePercent.toInt()}%", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(FicsitTheme.colors.border),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (usagePercent / 100).toFloat())
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (circuit.fuseTriggered) FicsitTheme.colors.accentRed
                        else MaterialTheme.colorScheme.primary,
                    ),
            )
        }
    }
}

@Composable
private fun MetricColumn(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = FicsitTheme.colors.textSecondary)
        Text(value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
    }
}
