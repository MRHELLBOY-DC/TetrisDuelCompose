package com.example.tetrisduel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tetrisduel.ui.screens.GameScreen
import com.example.tetrisduel.ui.screens.LobbyScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOBBY
    ) {
        composable(Routes.LOBBY) {
            LobbyScreen(
                onStartGame = {
                    navController.navigate(Routes.GAME)
                }
            )
        }

        composable(Routes.GAME) {
            GameScreen()
        }
    }
}