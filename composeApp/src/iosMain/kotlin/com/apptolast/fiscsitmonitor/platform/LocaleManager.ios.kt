package com.apptolast.fiscsitmonitor.platform

import platform.Foundation.NSUserDefaults

actual fun applyAppLocale(tag: String?) {
    val defaults = NSUserDefaults.standardUserDefaults
    if (tag.isNullOrBlank()) {
        defaults.removeObjectForKey("AppleLanguages")
    } else {
        defaults.setObject(listOf(tag), forKey = "AppleLanguages")
    }
    defaults.synchronize()
}
