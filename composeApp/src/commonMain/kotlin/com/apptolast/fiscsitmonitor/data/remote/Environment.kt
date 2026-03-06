package com.apptolast.fiscsitmonitor.data.remote

import com.apptolast.fiscsitmonitor.BuildKonfig

object Environment {
    val baseUrl: String = BuildKonfig.API_BASE_URL
    val wsAppKey: String = BuildKonfig.WS_APP_KEY

    val apiBaseUrl: String get() = "$baseUrl/api/v1"
}
