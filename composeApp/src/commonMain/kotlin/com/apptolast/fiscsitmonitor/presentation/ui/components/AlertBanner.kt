package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme

@Composable
fun AlertBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    val accentRed = FicsitTheme.colors.accentRed

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(FicsitTheme.colors.accentRed20)
            .drawBehind {
                drawLine(
                    color = accentRed,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 3.dp.toPx(),
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = FicsitTheme.colors.accentRed,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
            ),
            color = FicsitTheme.colors.accentRed,
        )
    }
}
