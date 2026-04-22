package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormError
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AddServerEvent {
    data object Created : AddServerEvent
}

class AddServerViewModel(
    private val userServerRepository: UserServerRepository,
    private val session: AuthSession,
) : ViewModel() {

    private val _state = MutableStateFlow(ServerFormState(requireAdminPassword = true))
    val state: StateFlow<ServerFormState> = _state.asStateFlow()

    private val _events = MutableStateFlow<AddServerEvent?>(null)
    val events: StateFlow<AddServerEvent?> = _events.asStateFlow()

    fun onNameChange(value: String) =
        update { it.copy(name = value, error = null, fieldErrors = it.fieldErrors - "name") }

    fun onHostChange(value: String) =
        update { it.copy(host = value, error = null, fieldErrors = it.fieldErrors - "host") }

    fun onApiPortChange(value: String) =
        update { it.copy(apiPort = value, error = null, fieldErrors = it.fieldErrors - "api_port") }

    fun onFrmHttpPortChange(value: String) =
        update { it.copy(frmHttpPort = value, error = null, fieldErrors = it.fieldErrors - "frm_http_port") }

    fun onFrmWsPortChange(value: String) =
        update { it.copy(frmWsPort = value, error = null, fieldErrors = it.fieldErrors - "frm_ws_port") }

    fun onSchemeChange(value: String) =
        update { it.copy(scheme = value, error = null) }

    fun onPathPrefixChange(value: String) =
        update { it.copy(pathPrefix = value, error = null, fieldErrors = it.fieldErrors - "path_prefix") }

    fun onVerifyTlsChange(value: Boolean) =
        update { it.copy(verifyTls = value, error = null) }

    fun onAdvancedExpandedChange(value: Boolean) =
        update { it.copy(advancedExpanded = value) }

    fun onAdminPasswordChange(value: String) =
        update { it.copy(adminPassword = value, error = null, fieldErrors = it.fieldErrors - "admin_password") }

    fun consumeEvent() {
        _events.value = null
    }

    fun submit() {
        val current = _state.value
        val localErrors = current.validateLocal()
        if (localErrors.isNotEmpty()) {
            _state.value = current.copy(error = ServerFormError.Validation, fieldErrors = localErrors)
            return
        }
        if (current.isSubmitting) return
        _state.value = current.copy(isSubmitting = true, error = null, fieldErrors = emptyMap())
        viewModelScope.launch {
            try {
                val created = userServerRepository.create(
                    name = current.name.trim(),
                    host = current.host.trim(),
                    scheme = current.scheme,
                    apiPort = current.apiPortInt ?: 7777,
                    pathPrefix = current.pathPrefix.trim(),
                    verifyTls = current.verifyTls,
                    frmHttpPort = current.frmHttpPortInt ?: 8080,
                    frmWsPort = current.frmWsPortInt ?: 8081,
                    adminPassword = current.adminPassword,
                )
                session.onServerSelected(created.id)
                _state.value = _state.value.copy(isSubmitting = false, adminPassword = "")
                _events.value = AddServerEvent.Created
            } catch (e: AuthError.Validation) {
                val fieldErrors = e.fieldErrors.mapValues { it.value.firstOrNull().orEmpty() }
                val topLevelError = when {
                    fieldErrors["admin_password"] == "provisioning_failed" -> ServerFormError.ProvisioningFailed
                    fieldErrors.containsKey("host") -> ServerFormError.Unreachable
                    else -> ServerFormError.Validation
                }
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    error = topLevelError,
                    fieldErrors = fieldErrors,
                )
            } catch (e: AuthError.Network) {
                _state.value = _state.value.copy(isSubmitting = false, error = ServerFormError.Network)
            } catch (e: Throwable) {
                _state.value = _state.value.copy(isSubmitting = false, error = ServerFormError.Generic)
            }
        }
    }

    private inline fun update(transform: (ServerFormState) -> ServerFormState) {
        _state.value = transform(_state.value)
    }
}
