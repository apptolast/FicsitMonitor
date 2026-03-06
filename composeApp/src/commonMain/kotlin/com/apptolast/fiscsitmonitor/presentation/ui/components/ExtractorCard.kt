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

@Composable
fun ExtractorCard(
    extractor: ExtractorDto,
    modifier: Modifier = Modifier,
) {
    val status = if (extractor.isProducing) "ACTIVE" to BadgeType.SUCCESS else "STOPPED" to BadgeType.ERROR

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
            StatusBadge(text = status.first, type = status.second)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("CURRENT PROD", style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(extractor.currentProd.formatRate(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("MAX PROD", style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(extractor.maxProd.formatRate(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("EFFICIENCY", style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(extractor.prodPercent.formatPercent(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
