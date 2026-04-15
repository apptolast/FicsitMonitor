package com.apptolast.fiscsitmonitor.domain.util

sealed class AuthError : Exception() {
    data object InvalidCredentials : AuthError()
    data class Validation(val fieldErrors: Map<String, List<String>>) : AuthError()
    data object Network : AuthError()
    data class Unknown(val reason: String?) : AuthError()
}
