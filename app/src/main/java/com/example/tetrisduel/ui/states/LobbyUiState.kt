package com.example.tetrisduel.ui.states

data class LobbyUiState(
    val playerName: String = "",
    val roomCode: String = "",
    val generatedRoomCode: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isConnectedToServer: Boolean = false,
    val isWaitingForOpponent: Boolean = false
)