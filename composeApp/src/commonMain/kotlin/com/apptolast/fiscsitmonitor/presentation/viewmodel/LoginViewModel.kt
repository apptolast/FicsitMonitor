package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.domain.repository.AuthRepository
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val error: LoginError? = null,
)

enum class LoginError { InvalidCredentials, Validation, Network, Generic }

sealed interface LoginEvent {
    data class LoggedIn(val hasServer: Boolean) : LoginEvent
}

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userServerRepository: UserServerRepository,
    private val session: AuthSession,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = MutableStateFlow<LoginEvent?>(null)
    val events: StateFlow<LoginEvent?> = _events.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password, error = null)
    }

    fun consumeEvent() {
        _events.value = null
    }

    fun submit() {
        val current = _state.value
        if (current.email.isBlank() || current.password.isBlank()) {
            _state.value = current.copy(error = LoginError.Validation)
            return
        }
        if (current.isSubmitting) return

        _state.value = current.copy(isSubmitting = true, error = null)
        viewModelScope.launch {
            try {
                authRepository.login(email = current.email.trim(), password = current.password)
                val servers = runCatching { userServerRepository.list() }.getOrDefault(emptyList())
                val firstId = servers.firstOrNull()?.id
                session.onServerSelected(firstId)
                _state.value = _state.value.copy(isSubmitting = false)
                _events.value = LoginEvent.LoggedIn(hasServer = firstId != null)
            } catch (e: AuthError.InvalidCredentials) {
                _state.value = _state.value.copy(isSubmitting = false, error = LoginError.InvalidCredentials)
            } catch (e: AuthError.Validation) {
                _state.value = _state.value.copy(isSubmitting = false, error = LoginError.Validation)
            } catch (e: AuthError.Network) {
                _state.value = _state.value.copy(isSubmitting = false, error = LoginError.Network)
            } catch (e: Throwable) {
                _state.value = _state.value.copy(isSubmitting = false, error = LoginError.Generic)
            }
        }
    }
}
