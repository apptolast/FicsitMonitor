package com.apptolast.fiscsitmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.fiscsitmonitor.data.bootstrap.ServerBootstrapper
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.domain.model.SessionState
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SplashDestination {
    data object Pending : SplashDestination
    data object Login : SplashDestination
    data object AddServer : SplashDestination
    data object Main : SplashDestination
}

class SplashViewModel(
    private val session: AuthSession,
    private val userServerRepository: UserServerRepository,
    private val bootstrapper: ServerBootstrapper,
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Pending)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        bootstrapper.start()
        decide()
    }

    private fun decide() {
        viewModelScope.launch {
            session.hydrate()
            val state = session.state.value
            if (state !is SessionState.Authenticated) {
                _destination.value = SplashDestination.Login
                return@launch
            }
            val servers = runCatching { userServerRepository.list() }.getOrDefault(emptyList())
            if (servers.isEmpty()) {
                session.onServerSelected(null)
                _destination.value = SplashDestination.AddServer
                return@launch
            }
            val preselected = state.selectedServerId?.takeIf { id -> servers.any { it.id == id } }
                ?: servers.first().id
            session.onServerSelected(preselected)
            _destination.value = SplashDestination.Main
        }
    }
}
