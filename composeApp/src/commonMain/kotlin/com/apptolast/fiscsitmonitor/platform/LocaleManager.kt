package com.apptolast.fiscsitmonitor.platform

val supportedLocales: List<String> = listOf("en", "es", "nl", "de")

expect fun applyAppLocale(tag: String?)
