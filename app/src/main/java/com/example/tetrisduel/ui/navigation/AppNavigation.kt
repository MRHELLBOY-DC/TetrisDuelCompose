package com.example.tetrisduel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tetrisduel.ui.screens.GameScreen
import com.example.tetrisduel.ui.screens.LobbyScreen
import com.example.tetrisduel.ui.screens.ResultScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOBBY
    ) {
        composable(Routes.LOBBY) {
            LobbyScreen(
                onNavigateToGame = { playerName, roomId, isOnline ->
                    navController.navigate(Routes.gameRoute(playerName, roomId, isOnline))
                }
            )
        }

        composable(
            route = Routes.GAME_ROUTE,
            arguments = listOf(
                navArgument("playerName") { type = NavType.StringType; defaultValue = "Jugador" },
                navArgument("roomCode") { type = NavType.StringType; defaultValue = "" },
                navArgument("isOnline") { type = NavType.BoolType; defaultValue = false }
            )
        ) {
            GameScreen(
                onNavigateToResult = { result ->
                    navController.navigate(
                        Routes.resultRoute(
                            winnerName = result.winnerName,
                            isWin = result.isLocalPlayerWinner,
                            score = result.score,
                            linesCleared = result.linesCleared,
                            duration = result.durationSeconds
                        )
                    ) {
                        popUpTo(Routes.LOBBY) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Routes.RESULT_ROUTE,
            arguments = listOf(
                navArgument("winnerName") { type = NavType.StringType; defaultValue = "" },
                navArgument("isWin") { type = NavType.BoolType; defaultValue = false },
                navArgument("score") { type = NavType.IntType; defaultValue = 0 },
                navArgument("linesCleared") { type = NavType.IntType; defaultValue = 0 },
                navArgument("duration") { type = NavType.LongType; defaultValue = 0L }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            ResultScreen(
                winnerName = args.getString("winnerName") ?: "",
                isLocalPlayerWinner = args.getBoolean("isWin"),
                score = args.getInt("score"),
                linesCleared = args.getInt("linesCleared"),
                durationSeconds = args.getLong("duration"),
                onBackToLobby = {
                    navController.navigate(Routes.LOBBY) {
                        popUpTo(Routes.LOBBY) { inclusive = true }
                    }
                }
            )
        }
    }
}