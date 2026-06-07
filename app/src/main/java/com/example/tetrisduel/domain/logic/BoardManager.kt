package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.Cell
import com.example.tetrisduel.domain.models.CellState
import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.Position

class BoardManager (
    private val pieceRotationManager: PieceRotationManager
) {
    private fun isValidIndex(board: Board, position: Position): Boolean {
        return position.row in 0 until board.rows &&
                position.column in 0 until board.columns
    }

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

    fun getCell(board: Board, position: Position): Cell? {
        if (!isValidIndex(board, position)) return null

        return board.cells[position.row][position.column]
    }

    fun updateCell(board: Board, updatedCell: Cell): Board {
        if (!isValidIndex(board, updatedCell.position)) return board

        val newCells = board.cells.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, currentCell ->
                if (
                    rowIndex == updatedCell.position.row &&
                    columnIndex == updatedCell.position.column
                ) {
                    updatedCell
                } else {
                    currentCell
                }
            }
        }

        return board.copy(cells = newCells)
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