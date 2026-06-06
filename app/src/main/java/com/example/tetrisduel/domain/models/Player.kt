package com.example.tetrisduel.domain.models

data class Player(
    val id: String,
    val name: String,
    val board: Board,
    val score: Int = 0,
    val linesCleared: Int = 0,
    val status: PlayerStatus = PlayerStatus.WAITING,
)