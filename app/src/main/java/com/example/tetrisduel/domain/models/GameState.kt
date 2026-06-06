package com.example.tetrisduel.domain.models

data class GameState (
    val localPlayer: Player,
    val opponentPlayer: Player? = null,
    val currentPiece: Piece? = null,
    val nextPiece: Piece? = null,
    val heldPiece: Piece? = null,
    val isGameRunning: Boolean = false,
    val result: GameResult? = null
)