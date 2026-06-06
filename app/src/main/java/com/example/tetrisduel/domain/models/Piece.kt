package com.example.tetrisduel.domain.models

data class Piece(
    val type: PieceType,
    val pivot: Position,
    val rotation: Rotation,
)



