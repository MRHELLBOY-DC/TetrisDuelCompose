package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.Cell
import com.example.tetrisduel.domain.models.CellState
import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.Position

class BoardManager (
    private val pieceRotationManager: PieceRotationManager
) {
    fun createEmptyBoard(rows: Int = 20, columns: Int = 10): Board {
        val cells = List(rows) { row ->
            List(columns) { column ->
                Cell(
                    position = Position(row = row, column = column),
                    state = CellState.EMPTY,
                    pieceType = null
                )
            }
        }

        return Board(
            rows = rows,
            columns = columns,
            cells = cells
        )
    }

    fun isInsideBoard(board: Board, position: Position): Boolean{
        return position.row in 0 until board.rows
                && position.column in 0 until board.columns
    }

    fun isCellEmpty(board: Board, position: Position): Boolean {
        if (!isInsideBoard(board, position)) return false

        return board.cells[position.row][position.column].state == CellState.EMPTY
    }

    fun lockPiece(board: Board, piece: Piece): Board{
        val piecePositions = pieceRotationManager.getPiecePositions(piece = piece)

        val newCells = board.cells.map { row ->
            row.map { cell ->
                if(piecePositions.contains(cell.position)) {
                    cell.copy(
                        state = CellState.FILLED,
                        pieceType = piece.type
                    )
                } else {
                    cell
                }
            }
        }

        return board.copy(cells = newCells)
    }
}