package com.bingoapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bingoapp.ui.navigation.BingoBottomNavBar
import com.bingoapp.ui.navigation.NavGraph
import com.bingoapp.ui.navigation.Screen
import com.bingoapp.ui.navigation.bottomNavItems

@Composable
fun BingoAppRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Lobby.route,
        Screen.Profile.route,
        Screen.Info.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BingoBottomNavBar(
                    currentRoute = currentRoute,
                    onItemClick = { item ->
                        navController.navigate(item.route) {
                            popUpTo(Screen.Lobby.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavGraph(navController = navController)
    }
}
