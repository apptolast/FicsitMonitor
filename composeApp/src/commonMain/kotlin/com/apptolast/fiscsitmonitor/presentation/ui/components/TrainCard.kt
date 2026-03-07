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
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.fallback_train
import ficsitmonitor.composeapp.generated.resources.label_speed
import ficsitmonitor.composeapp.generated.resources.label_station
import ficsitmonitor.composeapp.generated.resources.no_data
import ficsitmonitor.composeapp.generated.resources.status_derailed
import ficsitmonitor.composeapp.generated.resources.status_self_driving
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrainCard(
    train: TrainDto,
    modifier: Modifier = Modifier,
) {
    val statusText: String
    val statusType: BadgeType
    when {
        train.isDerailed -> {
            statusText = stringResource(Res.string.status_derailed)
            statusType = BadgeType.ERROR
        }
        train.selfDriving == true -> {
            statusText = stringResource(Res.string.status_self_driving)
            statusType = BadgeType.SUCCESS
        }
        else -> {
            statusText = train.status?.uppercase() ?: ""
            statusType = BadgeType.WARNING
        }
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
            Text(train.name.ifEmpty { stringResource(Res.string.fallback_train) }, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            StatusBadge(text = statusText, type = statusType)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_speed), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text("${train.forwardSpeed.formatDecimal(0)} km/h", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(Res.string.label_station), style = MaterialTheme.typography.labelSmall, color = FicsitTheme.colors.textMuted)
                Text(train.currentStation?.ifEmpty { noData } ?: noData,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
