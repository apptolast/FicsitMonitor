package com.apptolast.fiscsitmonitor.domain.repository

import com.apptolast.fiscsitmonitor.domain.model.User

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String, passwordConfirmation: String): User
    suspend fun login(email: String, password: String): User
    suspend fun logout()
    suspend fun refreshCurrentUser(): User?
    suspend fun updateLocale(locale: String): User
}
