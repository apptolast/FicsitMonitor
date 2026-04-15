package com.apptolast.fiscsitmonitor.data.remote

import com.apptolast.fiscsitmonitor.BuildKonfig
import com.apptolast.fiscsitmonitor.data.session.SessionStorage

class Environment(private val storage: SessionStorage) {

    fun currentBaseUrl(): String = normalize(storage.overrideBaseUrl ?: BuildKonfig.API_BASE_URL)

    fun currentApiV1Base(): String = "${currentBaseUrl()}/api/v1/"

    fun currentApiBase(): String = "${currentBaseUrl()}/api/"

    private fun normalize(raw: String): String = raw.trimEnd('/')
}
