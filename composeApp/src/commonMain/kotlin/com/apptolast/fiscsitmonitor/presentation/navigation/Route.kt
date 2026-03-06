package com.apptolast.fiscsitmonitor.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Energy : Route

    @Serializable
    data object Factory : Route

    @Serializable
    data object Logistics : Route

    @Serializable
    data object Live : Route
}
