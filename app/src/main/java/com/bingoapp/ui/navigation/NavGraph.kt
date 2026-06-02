package com.bingoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bingoapp.ui.auth.LoginScreen
import com.bingoapp.ui.auth.OtpScreen
import com.bingoapp.ui.auth.UsernameSetupScreen
import com.bingoapp.ui.splash.SplashScreen
import com.bingoapp.ui.lobby.LobbyScreen
import com.bingoapp.ui.game.GameScreen
import com.bingoapp.ui.winner.WinnerScreen
import com.bingoapp.ui.profile.ProfileScreen
import com.bingoapp.ui.info.InfoScreen
import com.bingoapp.ui.admin.AdminScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLobby = {
                    navController.navigate(Screen.Lobby.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToUsernameSetup = {
                    navController.navigate(Screen.UsernameSetup.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onOtpSent = { verificationId ->
                    navController.navigate(Screen.OtpVerification.createRoute(verificationId))
                }
            )
        }

        composable(
            route = "otp/{verificationId}",
            arguments = listOf(navArgument("verificationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            OtpScreen(
                verificationId = verificationId,
                onVerified = { needsUsername ->
                    if (needsUsername) {
                        navController.navigate(Screen.UsernameSetup.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Lobby.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.UsernameSetup.route) {
            UsernameSetupScreen(
                onUsernameSet = {
                    navController.navigate(Screen.Lobby.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Lobby.route) {
            LobbyScreen(
                onNavigateToGame = { gameId ->
                    navController.navigate(Screen.Game.createRoute(gameId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToInfo = {
                    navController.navigate(Screen.Info.route)
                },
                onNavigateToAdmin = {
                    navController.navigate(Screen.Admin.route)
                }
            )
        }

        composable(
            route = "game/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            GameScreen(
                gameId = gameId,
                onGameEnd = { winnerGameId ->
                    navController.navigate(Screen.Winner.createRoute(winnerGameId)) {
                        popUpTo(Screen.Lobby.route)
                    }
                },
                onBackToLobby = {
                    navController.navigate(Screen.Lobby.route) {
                        popUpTo(Screen.Game.createRoute(gameId)) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "winner/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            WinnerScreen(
                gameId = gameId,
                onPlayAgain = {
                    navController.navigate(Screen.Lobby.route) {
                        popUpTo(Screen.Lobby.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Info.route) {
            InfoScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Admin.route) {
            AdminScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
