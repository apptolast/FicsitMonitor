package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveEvent

@Composable
fun EventCard(
    event: LiveEvent,
    modifier: Modifier = Modifier,
) {
    val icon = eventIcon(event.icon)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(FicsitTheme.colors.bgCard)
            .border(1.dp, FicsitTheme.colors.border, MaterialTheme.shapes.medium)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FicsitTheme.colors.accentCyan,
            modifier = Modifier.size(20.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = event.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = FicsitTheme.colors.textSecondary,
            )
        }
        Text(
            text = event.timestamp,
            style = MaterialTheme.typography.labelSmall,
            color = FicsitTheme.colors.textMuted,
        )
    }
}

private fun eventIcon(iconName: String): ImageVector = when (iconName) {
    "bolt" -> Icons.Default.Bolt
    "train" -> Icons.Default.Train
    "warning" -> Icons.Default.Warning
    "groups" -> Icons.Default.Groups
    "factory" -> Icons.Default.PrecisionManufacturing
    else -> Icons.Default.Notifications
}
