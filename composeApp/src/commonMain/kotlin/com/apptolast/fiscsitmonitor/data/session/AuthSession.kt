package com.apptolast.fiscsitmonitor.data.session

import com.apptolast.fiscsitmonitor.domain.model.SessionState
import com.apptolast.fiscsitmonitor.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthSession(private val storage: SessionStorage) {

    private val _state = MutableStateFlow<SessionState>(SessionState.Loading)
    val state: StateFlow<SessionState> = _state.asStateFlow()

    fun hydrate() {
        val token = storage.authToken
        val user = storage.user
        _state.value = if (token != null && user != null) {
            SessionState.Authenticated(token, user, storage.selectedServerId)
        } else {
            SessionState.Unauthenticated
        }
    }

    fun onLogin(token: String, user: User) {
        storage.authToken = token
        storage.user = user
        _state.value = SessionState.Authenticated(token, user, storage.selectedServerId)
    }

    fun onLogout() {
        storage.clearSession()
        _state.value = SessionState.Unauthenticated
    }

    fun onServerSelected(serverId: Int?) {
        storage.selectedServerId = serverId
        val current = _state.value
        if (current is SessionState.Authenticated) {
            _state.value = current.copy(selectedServerId = serverId)
        }
    }

    fun currentToken(): String? = (state.value as? SessionState.Authenticated)?.token ?: storage.authToken
    fun currentServerId(): Int? =
        (state.value as? SessionState.Authenticated)?.selectedServerId ?: storage.selectedServerId

    fun currentUser(): User? = (state.value as? SessionState.Authenticated)?.user ?: storage.user

    fun currentLocale(): String? = storage.userLocale

    fun setLocale(tag: String?) {
        storage.userLocale = tag
    }
}
