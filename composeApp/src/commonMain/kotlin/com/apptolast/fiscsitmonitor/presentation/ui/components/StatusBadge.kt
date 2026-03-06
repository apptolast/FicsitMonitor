package com.apptolast.fiscsitmonitor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitTheme

enum class BadgeType { SUCCESS, ERROR, WARNING, INFO }

@Composable
fun StatusBadge(
    text: String,
    type: BadgeType,
    modifier: Modifier = Modifier,
) {
    val (dotColor, textColor, bgColor) = when (type) {
        BadgeType.SUCCESS -> Triple(
            FicsitTheme.colors.accentGreen,
            FicsitTheme.colors.accentGreen,
            FicsitTheme.colors.accentGreen20,
        )
        BadgeType.ERROR -> Triple(
            FicsitTheme.colors.accentRed,
            FicsitTheme.colors.accentRed,
            FicsitTheme.colors.accentRed20,
        )
        BadgeType.WARNING -> Triple(
            FicsitTheme.colors.accentOrange,
            FicsitTheme.colors.accentOrange,
            FicsitTheme.colors.accentOrange20,
        )
        BadgeType.INFO -> Triple(
            FicsitTheme.colors.accentCyan,
            FicsitTheme.colors.accentCyan,
            Color(0x3300BFFF),
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(36.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
        )
    }
}
