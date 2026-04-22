package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.bootstrap.ServerBootstrapper
import com.apptolast.fiscsitmonitor.data.remote.websocket.ReverbWebSocketClient
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.data.session.SessionStorage
import com.apptolast.fiscsitmonitor.domain.model.SessionState
import com.apptolast.fiscsitmonitor.domain.model.UserServer
import com.apptolast.fiscsitmonitor.domain.repository.AuthRepository
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormError
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isLoading: Boolean = true,
    val servers: List<UserServer> = emptyList(),
    val selectedServerId: Int? = null,
    val form: ServerFormState = ServerFormState(),
    val baseUrlOverride: String = "",
    val isSaved: Boolean = false,
    val loadError: String? = null,
)

sealed interface SettingsEvent {
    data object LoggedOut : SettingsEvent
}

class SettingsViewModel(
    private val userServerRepository: UserServerRepository,
    private val authRepository: AuthRepository,
    private val session: AuthSession,
    private val storage: SessionStorage,
    private val webSocketClient: ReverbWebSocketClient,
    private val bootstrapper: ServerBootstrapper,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val _events = MutableStateFlow<SettingsEvent?>(null)
    val events: StateFlow<SettingsEvent?> = _events.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, loadError = null)
            try {
                val servers = userServerRepository.list()
                val selectedId = (session.state.value as? SessionState.Authenticated)?.selectedServerId
                    ?: servers.firstOrNull()?.id
                val current = servers.firstOrNull { it.id == selectedId } ?: servers.firstOrNull()
                _state.value = _state.value.copy(
                    isLoading = false,
                    servers = servers,
                    selectedServerId = current?.id,
                    form = current?.let { ServerFormState.fromUserServer(it) } ?: ServerFormState(),
                    baseUrlOverride = storage.overrideBaseUrl.orEmpty(),
                )
            } catch (t: Throwable) {
                _state.value = _state.value.copy(isLoading = false, loadError = t.message)
            }
        }
    }

    fun onNameChange(value: String) =
        updateForm { it.copy(name = value, fieldErrors = it.fieldErrors - "name", error = null) }

    fun onHostChange(value: String) =
        updateForm { it.copy(host = value, fieldErrors = it.fieldErrors - "host", error = null) }

    fun onApiPortChange(value: String) =
        updateForm { it.copy(apiPort = value, fieldErrors = it.fieldErrors - "api_port", error = null) }

    fun onFrmHttpPortChange(value: String) =
        updateForm { it.copy(frmHttpPort = value, fieldErrors = it.fieldErrors - "frm_http_port", error = null) }

    fun onFrmWsPortChange(value: String) =
        updateForm { it.copy(frmWsPort = value, fieldErrors = it.fieldErrors - "frm_ws_port", error = null) }

    fun onSchemeChange(value: String) =
        updateForm { it.copy(scheme = value, error = null) }

    fun onPathPrefixChange(value: String) =
        updateForm { it.copy(pathPrefix = value, fieldErrors = it.fieldErrors - "path_prefix", error = null) }

    fun onVerifyTlsChange(value: Boolean) =
        updateForm { it.copy(verifyTls = value, error = null) }

    fun onAdvancedExpandedChange(value: Boolean) =
        updateForm { it.copy(advancedExpanded = value) }

    fun onBaseUrlChange(value: String) {
        _state.value = _state.value.copy(baseUrlOverride = value, isSaved = false)
    }

    fun onSelectServer(serverId: Int) {
        val server = _state.value.servers.firstOrNull { it.id == serverId } ?: return
        _state.value = _state.value.copy(
            selectedServerId = serverId,
            form = ServerFormState.fromUserServer(server),
            isSaved = false,
        )
    }

    fun saveServer() {
        val current = _state.value
        val serverId = current.selectedServerId ?: return
        val localErrors = current.form.validateLocal()
        if (localErrors.isNotEmpty()) {
            _state.value =
                current.copy(form = current.form.copy(error = ServerFormError.Validation, fieldErrors = localErrors))
            return
        }
        if (current.form.isSubmitting) return
        _state.value = current.copy(
            form = current.form.copy(isSubmitting = true, error = null, fieldErrors = emptyMap()),
            isSaved = false
        )

        viewModelScope.launch {
            try {
                storage.overrideBaseUrl = current.baseUrlOverride.trim().ifBlank { null }
                val updated = userServerRepository.update(
                    serverId = serverId,
                    name = current.form.name.trim(),
                    host = current.form.host.trim(),
                    scheme = current.form.scheme,
                    apiPort = current.form.apiPortInt ?: 7777,
                    pathPrefix = current.form.pathPrefix.trim(),
                    verifyTls = current.form.verifyTls,
                    frmHttpPort = current.form.frmHttpPortInt ?: 8080,
                    frmWsPort = current.form.frmWsPortInt ?: 8081,
                )
                webSocketClient.invalidateConfigCache()
                bootstrapper.refreshSnapshot(updated.id)
                _state.value = _state.value.copy(
                    form = ServerFormState.fromUserServer(updated),
                    isSaved = true,
                )
            } catch (e: AuthError.Validation) {
                _state.value = _state.value.copy(
                    form = _state.value.form.copy(
                        isSubmitting = false,
                        error = ServerFormError.Validation,
                        fieldErrors = e.fieldErrors.mapValues { it.value.firstOrNull().orEmpty() },
                    ),
                )
            } catch (e: AuthError.Network) {
                _state.value = _state.value.copy(
                    form = _state.value.form.copy(isSubmitting = false, error = ServerFormError.Network),
                )
            } catch (e: Throwable) {
                _state.value = _state.value.copy(
                    form = _state.value.form.copy(isSubmitting = false, error = ServerFormError.Generic),
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _events.value = SettingsEvent.LoggedOut
        }
    }

    fun consumeEvent() {
        _events.value = null
    }

    private inline fun updateForm(transform: (ServerFormState) -> ServerFormState) {
        _state.value = _state.value.copy(form = transform(_state.value.form), isSaved = false)
    }
}
