package com.apptolast.fiscsitmonitor.presentation.ui.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class TabDestination(
    val route: Route,
    val titleRes: StringResource,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val tabDestinations: List<TabDestination> = listOf(
    TabDestination(Route.Home, Res.string.tab_home, Icons.Default.Dashboard),
    TabDestination(Route.Energy, Res.string.tab_energy, Icons.Default.Bolt),
    TabDestination(Route.Factory, Res.string.tab_factory, Icons.Default.PrecisionManufacturing),
    TabDestination(Route.Logistics, Res.string.tab_logistics, Icons.Default.LocalShipping),
    TabDestination(Route.Live, Res.string.tab_live, Icons.Default.Stream),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    onOpenSettings: () -> Unit = {},
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val selectedIndex = tabDestinations
        .indexOfFirst { tab -> currentDestination?.hasRoute(tab.route::class) == true }
        .coerceAtLeast(0)

    val tabs = tabDestinations.map { BottomBarTab(stringResource(it.titleRes), it.icon) }
    val topBarTitle = stringResource(tabDestinations[selectedIndex].titleRes)

    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text(text = topBarTitle) },
//                actions = {
//                    IconButton(onClick = onOpenSettings) {
//                        Icon(
//                            imageVector = Icons.Default.Settings,
//                            contentDescription = stringResource(Res.string.settings_title),
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                },
//            )
//        },
        bottomBar = {
            FicsitBottomBar(
                tabs = tabs,
                selectedIndex = selectedIndex,
                onTabSelected = { index ->
                    if (index != selectedIndex) {
                        navController.navigate(tabDestinations[index].route) {
                            popUpTo(Route.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp),
        ) {
            composable<Route.Home> { HomeScreen() }
            composable<Route.Energy> { EnergyScreen() }
            composable<Route.Factory> { FactoryScreen() }
            composable<Route.Logistics> { LogisticsScreen() }
            composable<Route.Live> { LiveScreen() }
        }
    }
}
