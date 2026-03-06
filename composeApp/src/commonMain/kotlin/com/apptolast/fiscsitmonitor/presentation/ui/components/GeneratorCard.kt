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
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatDecimal
import com.apptolast.fiscsitmonitor.util.formatMW
import com.apptolast.fiscsitmonitor.util.formatPercent
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.fallback_generator
import ficsitmonitor.composeapp.generated.resources.label_capacity
import ficsitmonitor.composeapp.generated.resources.label_fuel
import ficsitmonitor.composeapp.generated.resources.label_output
import org.jetbrains.compose.resources.stringResource

@Composable
fun GeneratorCard(
    generator: GeneratorDto,
    modifier: Modifier = Modifier,
) {
    val loadPercent = if (generator.baseProd > 0) {
        (generator.dynamicProd / generator.baseProd * 100)
    } else 0.0

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
                text = generator.name.ifEmpty { stringResource(Res.string.fallback_generator) },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = loadPercent.formatPercent(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SmallLabel(stringResource(Res.string.label_output), generator.dynamicProd.formatMW())
            SmallLabel(stringResource(Res.string.label_capacity), generator.baseProd.formatMW())
            SmallLabel(stringResource(Res.string.label_fuel), generator.fuelAmount.formatDecimal(1))
        }
    }
}

@Composable
private fun SmallLabel(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
    }
}
