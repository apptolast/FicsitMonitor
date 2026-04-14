package com.apptolast.fiscsitmonitor.data.session

import com.apptolast.fiscsitmonitor.domain.model.User
import com.russhwolf.settings.Settings

class SessionStorage(private val settings: Settings) {

    var authToken: String?
        get() = settings.getStringOrNull(KEY_AUTH_TOKEN)
        set(value) {
            if (value == null) settings.remove(KEY_AUTH_TOKEN) else settings.putString(KEY_AUTH_TOKEN, value)
        }

    var user: User?
        get() {
            val id = settings.getIntOrNull(KEY_USER_ID) ?: return null
            val name = settings.getStringOrNull(KEY_USER_NAME) ?: return null
            val email = settings.getStringOrNull(KEY_USER_EMAIL) ?: return null
            return User(id, name, email)
        }
        set(value) {
            if (value == null) {
                settings.remove(KEY_USER_ID)
                settings.remove(KEY_USER_NAME)
                settings.remove(KEY_USER_EMAIL)
            } else {
                settings.putInt(KEY_USER_ID, value.id)
                settings.putString(KEY_USER_NAME, value.name)
                settings.putString(KEY_USER_EMAIL, value.email)
            }
        }

    var selectedServerId: Int?
        get() = settings.getIntOrNull(KEY_SELECTED_SERVER_ID)
        set(value) {
            if (value == null) settings.remove(KEY_SELECTED_SERVER_ID) else settings.putInt(
                KEY_SELECTED_SERVER_ID,
                value
            )
        }

    var overrideBaseUrl: String?
        get() = settings.getStringOrNull(KEY_OVERRIDE_BASE_URL)?.takeIf { it.isNotBlank() }
        set(value) {
            if (value.isNullOrBlank()) settings.remove(KEY_OVERRIDE_BASE_URL) else settings.putString(
                KEY_OVERRIDE_BASE_URL,
                value
            )
        }

    fun clearSession() {
        settings.remove(KEY_AUTH_TOKEN)
        settings.remove(KEY_USER_ID)
        settings.remove(KEY_USER_NAME)
        settings.remove(KEY_USER_EMAIL)
        settings.remove(KEY_SELECTED_SERVER_ID)
        clearAllShadows()
    }

    fun putShadow(serverId: Int, frmWsPort: Int?) {
        if (frmWsPort == null) {
            settings.remove(shadowKey(serverId, SUFFIX_FRM_WS))
        } else {
            settings.putInt(shadowKey(serverId, SUFFIX_FRM_WS), frmWsPort)
        }
    }

    fun getShadowFrmWsPort(serverId: Int): Int? = settings.getIntOrNull(shadowKey(serverId, SUFFIX_FRM_WS))

    fun removeShadow(serverId: Int) {
        settings.remove(shadowKey(serverId, SUFFIX_FRM_WS))
    }

    private fun clearAllShadows() {
        settings.keys.filter { it.startsWith(PREFIX_SHADOW) }.forEach { settings.remove(it) }
    }

    private fun shadowKey(serverId: Int, suffix: String) = "${PREFIX_SHADOW}${serverId}_$suffix"

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "auth_user_id"
        private const val KEY_USER_NAME = "auth_user_name"
        private const val KEY_USER_EMAIL = "auth_user_email"
        private const val KEY_SELECTED_SERVER_ID = "selected_server_id"
        private const val KEY_OVERRIDE_BASE_URL = "override_base_url"
        private const val PREFIX_SHADOW = "shadow_server_"
        private const val SUFFIX_FRM_WS = "frm_ws_port"
    }
}
