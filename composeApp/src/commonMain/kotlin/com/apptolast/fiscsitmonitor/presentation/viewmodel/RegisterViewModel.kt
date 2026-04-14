package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.domain.repository.AuthRepository
import com.apptolast.fiscsitmonitor.domain.util.AuthError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val isSubmitting: Boolean = false,
    val error: RegisterError? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

enum class RegisterError { Validation, Network, Generic }

sealed interface RegisterEvent {
    data object Registered : RegisterEvent
}

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private val _events = MutableStateFlow<RegisterEvent?>(null)
    val events: StateFlow<RegisterEvent?> = _events.asStateFlow()

    fun onNameChange(value: String) {
        update { it.copy(name = value, error = null) }
    }

    fun onEmailChange(value: String) {
        update { it.copy(email = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        update { it.copy(password = value, error = null) }
    }

    fun onPasswordConfirmationChange(value: String) {
        update { it.copy(passwordConfirmation = value, error = null) }
    }

    fun consumeEvent() {
        _events.value = null
    }

    fun submit() {
        val current = _state.value
        val localErrors = validate(current)
        if (localErrors.isNotEmpty()) {
            _state.value = current.copy(error = RegisterError.Validation, fieldErrors = localErrors)
            return
        }
        if (current.isSubmitting) return
        _state.value = current.copy(isSubmitting = true, error = null, fieldErrors = emptyMap())
        viewModelScope.launch {
            try {
                authRepository.register(
                    name = current.name.trim(),
                    email = current.email.trim(),
                    password = current.password,
                    passwordConfirmation = current.passwordConfirmation,
                )
                _state.value = _state.value.copy(isSubmitting = false)
                _events.value = RegisterEvent.Registered
            } catch (e: AuthError.Validation) {
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    error = RegisterError.Validation,
                    fieldErrors = e.fieldErrors.mapValues { it.value.firstOrNull().orEmpty() },
                )
            } catch (e: AuthError.Network) {
                _state.value = _state.value.copy(isSubmitting = false, error = RegisterError.Network)
            } catch (e: Throwable) {
                _state.value = _state.value.copy(isSubmitting = false, error = RegisterError.Generic)
            }
        }
    }

    private fun validate(state: RegisterUiState): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (state.name.isBlank()) errors["name"] = "required"
        if (state.email.isBlank()) errors["email"] = "required"
        else if (!state.email.contains("@") || !state.email.contains(".")) errors["email"] = "invalid_email"
        if (state.password.length < 8) errors["password"] = "too_short"
        if (state.password != state.passwordConfirmation) errors["password_confirmation"] = "mismatch"
        return errors
    }

    private inline fun update(transform: (RegisterUiState) -> RegisterUiState) {
        _state.value = transform(_state.value)
    }
}
