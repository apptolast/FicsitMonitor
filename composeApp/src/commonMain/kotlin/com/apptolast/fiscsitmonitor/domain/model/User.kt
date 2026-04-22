package com.apptolast.fiscsitmonitor.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val locale: String? = null,
)
