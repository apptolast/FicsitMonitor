package com.apptolast.fiscsitmonitor.data.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class ValidationErrorResponse(
    val message: String? = null,
    val errors: Map<String, List<String>> = emptyMap(),
)
