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
import com.apptolast.fiscsitmonitor.data.model.ExtractorDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatPercent
import com.apptolast.fiscsitmonitor.util.formatRate
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.label_active
import ficsitmonitor.composeapp.generated.resources.label_current_prod
import ficsitmonitor.composeapp.generated.resources.label_efficiency
import ficsitmonitor.composeapp.generated.resources.label_max_prod
import ficsitmonitor.composeapp.generated.resources.label_stopped
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExtractorCard(
    extractor: ExtractorDto,
    modifier: Modifier = Modifier,
) {
    val statusText = if (extractor.isProducing) stringResource(Res.string.label_active) else stringResource(Res.string.label_stopped)
    val statusType = if (extractor.isProducing) BadgeType.SUCCESS else BadgeType.ERROR

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
                text = "${extractor.name} \u2014 ${extractor.itemName}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            StatusBadge(text = statusText, type = statusType)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_current_prod), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(extractor.currentProd.formatRate(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_max_prod), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(extractor.maxProd.formatRate(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_efficiency), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(extractor.prodPercent.formatPercent(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
