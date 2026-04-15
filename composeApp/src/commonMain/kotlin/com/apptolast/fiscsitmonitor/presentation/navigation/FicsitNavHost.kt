package com.apptolast.fiscsitmonitor.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.apptolast.fiscsitmonitor.presentation.ui.components.BottomBarTab
import com.apptolast.fiscsitmonitor.presentation.ui.components.FicsitBottomBar
import com.apptolast.fiscsitmonitor.presentation.ui.screens.auth.LoginScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.auth.RegisterScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.energy.EnergyScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.factory.FactoryScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.home.HomeScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.live.LiveScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.logistics.LogisticsScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.AddServerScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.settings.SettingsScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.splash.SplashScreen
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SplashDestination
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
    val icon: ImageVector,
)

private val tabDestinations: List<TabDestination> = listOf(
    TabDestination(Route.Home, Res.string.tab_home, Icons.Default.Dashboard),
    TabDestination(Route.Energy, Res.string.tab_energy, Icons.Default.Bolt),
    TabDestination(Route.Factory, Res.string.tab_factory, Icons.Default.PrecisionManufacturing),
    TabDestination(Route.Logistics, Res.string.tab_logistics, Icons.Default.LocalShipping),
    TabDestination(Route.Live, Res.string.tab_live, Icons.Default.Stream),
)

@Composable
fun FicsitNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val isInMainGraph = currentDestination
        ?.hierarchy
        ?.any { it.hasRoute(Route.Main::class) } == true

    val selectedIndex = tabDestinations
        .indexOfFirst { tab ->
            currentDestination?.hierarchy?.any { it.hasRoute(tab.route::class) } == true
        }
        .coerceAtLeast(0)

    val tabs = tabDestinations.map { BottomBarTab(stringResource(it.titleRes), it.icon) }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            AnimatedVisibility(
                visible = isInMainGraph,
                enter = slideInVertically { fullHeight -> fullHeight } + fadeIn(),
                exit = slideOutVertically { fullHeight -> fullHeight } + fadeOut(),
            ) {
                FicsitBottomBar(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        val target = tabDestinations[index].route
                        if (!currentDestination.isOn(target)) {
                            navController.navigate(target) {
                                popUpTo<Route.Home> { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable<Route.Splash> {
                SplashScreen(
                    onDestinationResolved = { destination ->
                        val target = when (destination) {
                            SplashDestination.Login -> Route.Login
                            SplashDestination.AddServer -> Route.AddServer
                            SplashDestination.Main -> Route.Main
                            SplashDestination.Pending -> return@SplashScreen
                        }
                        navController.navigate(target) {
                            popUpTo<Route.Splash> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable<Route.Login> {
                LoginScreen(
                    onLoggedIn = { hasServer ->
                        val target = if (hasServer) Route.Main else Route.AddServer
                        navController.navigate(target) {
                            popUpTo<Route.Login> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Route.Register) },
                )
            }

            composable<Route.Register> {
                RegisterScreen(
                    onRegistered = {
                        navController.navigate(Route.AddServer) {
                            popUpTo<Route.Login> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() },
                )
            }

            composable<Route.AddServer> {
                AddServerScreen(
                    onServerCreated = {
                        navController.navigate(Route.Main) {
                            popUpTo<Route.AddServer> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }

            navigation<Route.Main>(startDestination = Route.Home) {
                composable<Route.Home> { HomeScreen() }
                composable<Route.Energy> { EnergyScreen() }
                composable<Route.Factory> { FactoryScreen() }
                composable<Route.Logistics> { LogisticsScreen() }
                composable<Route.Live> { LiveScreen() }
            }

            composable<Route.Settings> {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLoggedOut = {
                        navController.navigate(Route.Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
    }
}

private fun NavDestination?.isOn(route: Route): Boolean =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true
