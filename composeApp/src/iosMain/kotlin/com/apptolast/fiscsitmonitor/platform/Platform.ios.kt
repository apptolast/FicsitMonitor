package com.apptolast.fiscsitmonitor.platform

import platform.UIKit.UIDevice

actual fun platformDeviceName(): String {
    val device = UIDevice.currentDevice
    val name = device.name
    return if (name.isNotBlank()) name else "${device.model} ${device.systemVersion}"
}
