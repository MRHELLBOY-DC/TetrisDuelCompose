package com.example.tetrisduel.domain.models

data class Cell(
    val position: Position,
    val state: CellState  = CellState.EMPTY,
    val pieceType: PieceType?= null,
)


