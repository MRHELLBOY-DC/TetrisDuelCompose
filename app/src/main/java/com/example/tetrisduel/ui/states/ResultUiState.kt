package com.example.tetrisduel.ui.states

data class ResultUiState(
    val winnerName: String = "",
    val isLocalPlayerWinner: Boolean = false,
    val score: Int = 0,
    val linesCleared: Int = 0,
    val durationSeconds: Long = 0L
)