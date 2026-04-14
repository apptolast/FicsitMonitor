package com.apptolast.fiscsitmonitor.presentation.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apptolast.fiscsitmonitor.presentation.navigation.Route
import com.apptolast.fiscsitmonitor.presentation.ui.components.BottomBarTab
import com.apptolast.fiscsitmonitor.presentation.ui.components.FicsitBottomBar
import com.apptolast.fiscsitmonitor.presentation.ui.screens.energy.EnergyScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.factory.FactoryScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.home.HomeScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.live.LiveScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.logistics.LogisticsScreen
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.tab_energy
import ficsitmonitor.composeapp.generated.resources.tab_factory
import ficsitmonitor.composeapp.generated.resources.tab_home
import ficsitmonitor.composeapp.generated.resources.tab_live
import ficsitmonitor.composeapp.generated.resources.tab_logistics
import org.jetbrains.compose.resources.stringResource

private val tabRoutes: List<Route> = listOf(
    Route.Home,
    Route.Energy,
    Route.Factory,
    Route.Logistics,
    Route.Live,
)

@Composable
fun MainScaffold(
    onOpenSettings: () -> Unit,
) {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        BottomBarTab(stringResource(Res.string.tab_home), Icons.Default.Dashboard),
        BottomBarTab(stringResource(Res.string.tab_energy), Icons.Default.Bolt),
        BottomBarTab(stringResource(Res.string.tab_factory), Icons.Default.PrecisionManufacturing),
        BottomBarTab(stringResource(Res.string.tab_logistics), Icons.Default.LocalShipping),
        BottomBarTab(stringResource(Res.string.tab_live), Icons.Default.Stream),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.weight(1f),
        ) {
            composable<Route.Home> { HomeScreen(onOpenSettings = onOpenSettings) }
            composable<Route.Energy> { EnergyScreen() }
            composable<Route.Factory> { FactoryScreen() }
            composable<Route.Logistics> { LogisticsScreen() }
            composable<Route.Live> { LiveScreen() }
        }
        FicsitBottomBar(
            tabs = tabs,
            selectedIndex = selectedTab,
            onTabSelected = { index ->
                if (index != selectedTab) {
                    selectedTab = index
                    navController.navigate(tabRoutes[index]) {
                        popUpTo(Route.Home) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
        )
    }
}
