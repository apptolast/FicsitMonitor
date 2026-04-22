package com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding

import com.apptolast.fiscsitmonitor.domain.model.UserServer

private const val ADMIN_PASSWORD_MIN_LENGTH = 8
const val DEFAULT_SCHEME = "https"
const val DEFAULT_PATH_PREFIX = "/api/v1"

data class ServerFormState(
    val name: String = "",
    val host: String = "",
    val scheme: String = DEFAULT_SCHEME,
    val apiPort: String = "7777",
    val pathPrefix: String = DEFAULT_PATH_PREFIX,
    val verifyTls: Boolean = false,
    val frmHttpPort: String = "8080",
    val frmWsPort: String = "8081",
    val adminPassword: String = "",
    val requireAdminPassword: Boolean = false,
    val advancedExpanded: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: ServerFormError? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
) {
    val apiPortInt: Int? get() = apiPort.toIntOrNull()
    val frmHttpPortInt: Int? get() = frmHttpPort.toIntOrNull()
    val frmWsPortInt: Int? get() = frmWsPort.toIntOrNull()

    fun isValid(): Boolean = validateLocal().isEmpty()

    fun validateLocal(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (name.isBlank()) errors["name"] = "required"
        if (host.isBlank()) errors["host"] = "required"
        if (!isValidPort(apiPort)) errors["api_port"] = "invalid_port"
        if (!isValidPort(frmHttpPort)) errors["frm_http_port"] = "invalid_port"
        if (!isValidPort(frmWsPort)) errors["frm_ws_port"] = "invalid_port"
        if (!pathPrefix.startsWith("/")) errors["path_prefix"] = "path_prefix_leading_slash"
        if (requireAdminPassword) {
            when {
                adminPassword.isBlank() -> errors["admin_password"] = "required"
                adminPassword.length < ADMIN_PASSWORD_MIN_LENGTH -> errors["admin_password"] = "too_short"
            }
        }
        return errors
    }

    private fun isValidPort(raw: String): Boolean {
        val value = raw.toIntOrNull() ?: return false
        return value in 1..65535
    }

    companion object {
        fun fromUserServer(server: UserServer): ServerFormState {
            val advanced = server.scheme != DEFAULT_SCHEME ||
                server.pathPrefix != DEFAULT_PATH_PREFIX ||
                server.verifyTls
            return ServerFormState(
                name = server.name,
                host = server.host,
                scheme = server.scheme,
                apiPort = server.apiPort.toString(),
                pathPrefix = server.pathPrefix,
                verifyTls = server.verifyTls,
                frmHttpPort = server.frmHttpPort.toString(),
                frmWsPort = server.frmWsPort.toString(),
                requireAdminPassword = false,
                advancedExpanded = advanced,
            )
        }
    }
}

enum class ServerFormError { Validation, Network, Unreachable, ProvisioningFailed, Generic }
