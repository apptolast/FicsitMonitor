package com.apptolast.fiscsitmonitor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apptolast.fiscsitmonitor.presentation.ui.screens.auth.LoginScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.auth.RegisterScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.navigation.NavigationScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.AddServerScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.settings.SettingsScreen
import com.apptolast.fiscsitmonitor.presentation.ui.screens.splash.SplashScreen
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SplashDestination

@Composable
fun FicsitNavHost(
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash,
        modifier = modifier,
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
                        popUpTo(Route.Splash) { inclusive = true }
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
                        popUpTo(Route.Login) { inclusive = true }
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
                        popUpTo(Route.Login) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.AddServer> {
            AddServerScreen(
                onServerCreated = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.AddServer) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Route.Main> {
            NavigationScreen(
                onOpenSettings = { navController.navigate(Route.Settings) },
            )
        }

        composable<Route.Settings> {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Main) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
