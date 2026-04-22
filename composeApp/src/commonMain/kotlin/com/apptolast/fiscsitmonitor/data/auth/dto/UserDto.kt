package com.apptolast.fiscsitmonitor.data.auth.dto

import com.apptolast.fiscsitmonitor.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val locale: String? = null,
) {
    fun toDomain(): User = User(id = id, name = name, email = email, locale = locale)
}
