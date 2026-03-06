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
import com.apptolast.fiscsitmonitor.data.model.TrainDto
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.util.formatDecimal

@Composable
fun TrainCard(
    train: TrainDto,
    modifier: Modifier = Modifier,
) {
    val status = when {
        train.isDerailed -> "DERAILED" to BadgeType.ERROR
        train.selfDriving -> "SELF-DRIVING" to BadgeType.SUCCESS
        else -> train.status.uppercase() to BadgeType.WARNING
    }

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
            Text(train.name.ifEmpty { "Train" }, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            StatusBadge(text = status.first, type = status.second)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("SPEED", style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text("${train.forwardSpeed.formatDecimal(0)} km/h", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("STATION", style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(train.currentStation.ifEmpty { "---" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
