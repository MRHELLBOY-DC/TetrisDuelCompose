package com.example.tetrisduel.domain.models

data class GameConfig(
    val boardRows: Int = 20,
    val boardColumns: Int = 10,
    val initialFallSpeedMillis: Long = 800L,
    val minimumFallSpeedMillis: Long = 100L
)
