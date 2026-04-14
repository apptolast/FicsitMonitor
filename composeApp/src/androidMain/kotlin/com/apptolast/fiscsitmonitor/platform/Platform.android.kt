package com.apptolast.fiscsitmonitor.platform

import android.os.Build

actual fun platformDeviceName(): String {
    val manufacturer = Build.MANUFACTURER?.takeIf { it.isNotBlank() } ?: "Android"
    val model = Build.MODEL?.takeIf { it.isNotBlank() } ?: Build.DEVICE ?: "device"
    return if (model.startsWith(manufacturer, ignoreCase = true)) model else "$manufacturer $model"
}
