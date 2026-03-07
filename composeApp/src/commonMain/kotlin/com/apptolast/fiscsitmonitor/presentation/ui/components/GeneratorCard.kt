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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
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
    val fuelFraction = if (generator.fuelInvMax > 0) {
        (generator.fuelInvAmount.toFloat() / generator.fuelInvMax).coerceIn(0f, 1f)
    } else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(FicsitTheme.colors.bgCard)
            .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Header: name + load %
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = generator.name.ifEmpty { stringResource(Res.string.fallback_generator) },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = generator.loadPct.formatPercent(),
                style = MaterialTheme.typography.bodyMedium,
                color = loadPercentColor(generator.loadPct),
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        // Metrics row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SmallLabel(stringResource(Res.string.label_output), generator.regulatedDemandMw.formatMW())
            SmallLabel(stringResource(Res.string.label_capacity), generator.capacityMw.formatMW())
            SmallLabel(
                stringResource(Res.string.label_fuel),
                if (generator.fuelResource.isNotEmpty()) {
                    "${generator.fuelResource} ${generator.fuelInvAmount}/${generator.fuelInvMax}"
                } else "---",
            )
        }

        // Inventory progress bar (red → orange → yellow → green gradient)
        if (generator.fuelInvMax > 0) {
            FuelInventoryBar(fraction = fuelFraction)
        }
    }
}

@Composable
private fun FuelInventoryBar(
    fraction: Float,
    modifier: Modifier = Modifier,
) {
    val barShape = RoundedCornerShape(3.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(barShape)
            .background(FicsitTheme.colors.border),
    ) {
        if (fraction > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = fraction)
                    .clip(barShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = fuelBarColors(fraction),
                        ),
                    ),
            )
        }
    }
}

private fun fuelBarColors(fraction: Float): List<Color> {
    val red = Color(0xFFFF4444)
    val orange = Color(0xFFFF8800)
    val yellow = Color(0xFFFFD700)
    val green = Color(0xFF00FF88)

    return when {
        fraction < 0.25f -> listOf(red, red)
        fraction < 0.50f -> listOf(red, orange)
        fraction < 0.75f -> listOf(red, orange, yellow)
        else -> listOf(red, orange, yellow, green)
    }
}

@Composable
private fun loadPercentColor(loadPct: Double): Color {
    return when {
        loadPct >= 90.0 -> FicsitTheme.colors.accentGreen
        loadPct >= 50.0 -> FicsitTheme.colors.accentYellow
        loadPct >= 10.0 -> FicsitTheme.colors.accentOrange
        else -> FicsitTheme.colors.accentRed
    }
}

@Composable
private fun SmallLabel(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = FicsitTheme.colors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
