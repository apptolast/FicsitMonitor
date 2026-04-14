package com.apptolast.fiscsitmonitor.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object AddServer : Route

    @Serializable
    data object Main : Route

    @Serializable
    data object Settings : Route

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
