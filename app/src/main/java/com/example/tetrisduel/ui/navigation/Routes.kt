package com.example.tetrisduel.ui.navigation

import android.net.Uri

object Routes {
    const val LOBBY = "lobby"

    const val GAME_ROUTE = "game?playerName={playerName}&roomCode={roomCode}&isOnline={isOnline}"
    const val RESULT_ROUTE = "result?winnerName={winnerName}&isWin={isWin}&score={score}&linesCleared={linesCleared}&duration={duration}"

    fun gameRoute(playerName: String, roomId: String, isOnline: Boolean) =
        "game?playerName=${Uri.encode(playerName)}&roomCode=${Uri.encode(roomId)}&isOnline=$isOnline"

    fun resultRoute(winnerName: String, isWin: Boolean, score: Int, linesCleared: Int, duration: Long) =
        "result?winnerName=${Uri.encode(winnerName)}&isWin=$isWin&score=$score&linesCleared=$linesCleared&duration=$duration"
}