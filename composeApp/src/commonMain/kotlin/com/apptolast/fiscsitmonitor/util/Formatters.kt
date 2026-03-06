package com.apptolast.fiscsitmonitor.util

import kotlin.math.roundToInt

fun Double.formatMW(): String = when {
    this >= 1000 -> "${(this / 1000).formatDecimal(1)} GW"
    else -> "${formatDecimal(1)} MW"
}

fun Double.formatDecimal(decimals: Int = 1): String {
    val factor = Math.pow(10.0, decimals.toDouble())
    val rounded = (this * factor).roundToInt() / factor
    return rounded.toString()
}

fun Double.formatPercent(): String = "${formatDecimal(1)}%"

fun Double.formatRate(): String = "${formatDecimal(2)}/min"

fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    return "${hours}h ${minutes.toString().padStart(2, '0')}m"
}

fun Double.formatTPS(): String = "${formatDecimal(1)} TPS"

private object Math {
    fun pow(base: Double, exp: Double): Double {
        var result = 1.0
        repeat(exp.toInt()) { result *= base }
        return result
    }
}
