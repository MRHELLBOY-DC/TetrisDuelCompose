package com.example.tetrisduel.ui.states

data class GameUiState(
    val isOnline: Boolean = false,
    val opponentName: String = "Oponente",
    val isOpponentConnected: Boolean = false,
    val isConnectedToServer: Boolean = false,
    val show37Banner: Boolean = false
)