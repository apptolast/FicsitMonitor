package com.apptolast.fiscsitmonitor.domain.model

sealed interface SessionState {
    data object Loading : SessionState
    data object Unauthenticated : SessionState
    data class Authenticated(
        val token: String,
        val user: User,
        val selectedServerId: Int?,
    ) : SessionState
}
