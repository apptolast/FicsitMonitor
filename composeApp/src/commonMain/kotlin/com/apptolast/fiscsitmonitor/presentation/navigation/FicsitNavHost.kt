package com.apptolast.fiscsitmonitor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.apptolast.fiscsitmonitor.presentation.ui.screens.energy.EnergyScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.factory.FactoryScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.home.HomeScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.live.LiveScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.logistics.LogisticsScreen

@Composable
fun FicsitNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier,
    ) {
        composable<Route.Home> { HomeScreen() }
        composable<Route.Energy> { EnergyScreen() }
        composable<Route.Factory> { FactoryScreen() }
        composable<Route.Logistics> { LogisticsScreen() }
        composable<Route.Live> { LiveScreen() }
    }
}
