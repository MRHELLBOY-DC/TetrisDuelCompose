package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.CellState
import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.Position

class CollisionDetector(
    private val pieceRotationManager: PieceRotationManager
) {

    fun canPlacePiece(board: Board, piece: Piece): Boolean {
        val positions = pieceRotationManager.getPiecePositions(piece)

        return positions.all { position ->
            isInsideBoard(board, position) && isCellEmpty(board, position)
        }
    }

    fun collides(board: Board, piece: Piece): Boolean {
        return !canPlacePiece(board, piece)
    }

    fun isInsideBoard(board: Board, position: Position): Boolean {
        return position.row in 0 until board.rows &&
                position.column in 0 until board.columns
    }

    fun isCellEmpty(board: Board, position: Position): Boolean {
        if (!isInsideBoard(board, position)) return false

        return board.cells[position.row][position.column].state == CellState.EMPTY
    }

    fun isGameOverPosition(board: Board, piece: Piece): Boolean {
        return collides(board, piece)
    }
}