package com.apptolast.fiscsitmonitor

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
import androidx.navigation.compose.rememberNavController
import com.apptolast.fiscsitmonitor.presentation.navigation.FicsitNavHost
import com.apptolast.fiscsitmonitor.presentation.navigation.Route
import com.apptolast.fiscsitmonitor.presentation.ui.components.BottomBarTab
import com.apptolast.fiscsitmonitor.presentation.ui.components.FicsitBottomBar
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme

private val tabs = listOf(
    BottomBarTab("HOME", Icons.Default.Dashboard),
    BottomBarTab("ENERGY", Icons.Default.Bolt),
    BottomBarTab("FACTORY", Icons.Default.PrecisionManufacturing),
    BottomBarTab("LOGISTICS", Icons.Default.LocalShipping),
    BottomBarTab("LIVE", Icons.Default.Stream),
)

private val routes: List<Route> = listOf(
    Route.Home,
    Route.Energy,
    Route.Factory,
    Route.Logistics,
    Route.Live,
)

@Composable
fun App() {
    FicsitMonitorTheme {
        val navController = rememberNavController()
        var selectedTab by rememberSaveable { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            FicsitNavHost(
                navController = navController,
                modifier = Modifier.weight(1f),
            )
            FicsitBottomBar(
                tabs = tabs,
                selectedIndex = selectedTab,
                onTabSelected = { index ->
                    if (index != selectedTab) {
                        selectedTab = index
                        navController.navigate(routes[index]) {
                            popUpTo(Route.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        }
    }
}
