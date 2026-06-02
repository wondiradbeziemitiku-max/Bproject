package com.bingoapp.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object OtpVerification : Screen("otp/{verificationId}") {
        fun createRoute(verificationId: String) = "otp/$verificationId"
    }
    data object UsernameSetup : Screen("username_setup")
    data object Lobby : Screen("lobby")
    data object Game : Screen("game/{gameId}") {
        fun createRoute(gameId: String) = "game/$gameId"
    }
    data object Winner : Screen("winner/{gameId}") {
        fun createRoute(gameId: String) = "winner/$gameId"
    }
    data object Profile : Screen("profile")
    data object Info : Screen("info")
    data object Admin : Screen("admin")
}
