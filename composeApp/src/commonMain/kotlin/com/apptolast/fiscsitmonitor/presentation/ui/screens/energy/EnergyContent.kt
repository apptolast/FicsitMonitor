package com.apptolast.fiscsitmonitor.presentation.ui.screens.energy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.data.model.GeneratorDto
import com.apptolast.fiscsitmonitor.data.model.PowerCircuitDto
import com.apptolast.fiscsitmonitor.presentation.ui.components.AlertBanner
import com.apptolast.fiscsitmonitor.presentation.ui.components.BadgeType
import com.apptolast.fiscsitmonitor.presentation.ui.components.CircuitCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.GeneratorCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.MetricCard
import com.apptolast.fiscsitmonitor.presentation.ui.components.SectionHeader
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatMW
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.alert_fuse_demand
import ficsitmonitor.composeapp.generated.resources.badge_every_15s
import ficsitmonitor.composeapp.generated.resources.label_capacity
import ficsitmonitor.composeapp.generated.resources.label_generators
import ficsitmonitor.composeapp.generated.resources.label_headroom
import ficsitmonitor.composeapp.generated.resources.section_electrical_grid
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnergyContent(
    circuits: List<PowerCircuitDto>,
    generators: List<GeneratorDto>,
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
        SectionHeader(
            title = stringResource(Res.string.section_electrical_grid),
            icon = Icons.Default.Bolt,
            iconTint = FicsitTheme.colors.accentYellow,
            badgeText = stringResource(Res.string.badge_every_15s),
            badgeType = BadgeType.SUCCESS,
        )

        if (fuseTriggered) {
            AlertBanner(message = stringResource(Res.string.alert_fuse_demand))
        }

        // Circuit cards
        circuits.forEach { circuit ->
            CircuitCard(circuit = circuit)
        }

        // Generators section
        if (generators.isNotEmpty()) {
            SectionHeader(
                title = stringResource(Res.string.label_generators),
                icon = Icons.Default.LocalFireDepartment,
                iconTint = FicsitTheme.colors.accentOrange,
                badgeText = stringResource(Res.string.badge_every_15s),
            )

            val totalCapacity = generators.sumOf { it.baseProd }
            val totalOutput = generators.sumOf { it.dynamicProd }
            val headroom = totalCapacity - totalOutput

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    label = stringResource(Res.string.label_generators),
                    value = "${generators.size}",
                    subtitle = "",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = FicsitTheme.colors.accentOrange,
                    modifier = Modifier.weight(1f),
                )
                MetricCard(
                    label = stringResource(Res.string.label_capacity),
                    value = totalCapacity.formatMW(),
                    subtitle = "",
                    icon = Icons.Default.Bolt,
                    iconTint = FicsitTheme.colors.accentYellow,
                    modifier = Modifier.weight(1f),
                )
                MetricCard(
                    label = stringResource(Res.string.label_headroom),
                    value = headroom.formatMW(),
                    subtitle = "",
                    icon = Icons.Default.Bolt,
                    modifier = Modifier.weight(1f),
                )
            }

            generators.forEach { generator ->
                GeneratorCard(generator = generator)
            }
        }
    }
}
