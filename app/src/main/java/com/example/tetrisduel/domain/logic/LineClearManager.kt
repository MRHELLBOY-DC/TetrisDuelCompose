package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.Cell
import com.example.tetrisduel.domain.models.CellState
import com.example.tetrisduel.domain.models.Position

class LineClearManager {
    data class LineClearResult(
        val board: Board,
        val linesCleared: Int
    )

    private fun createEmptyRow(row: Int, columns: Int): List<Cell> {
        return List(columns) { column ->
            Cell(
                position = Position(column = column, row= row),
                state = CellState.EMPTY,
                pieceType = null
            )
        }
    }

    fun clearCompletedLines(board: Board): LineClearResult {
        val incompleteRows = board.cells.filter { row ->
            row.any { cell -> cell.state != CellState.FILLED }
        }

        val linesCleared = board.rows - incompleteRows.size

        if(linesCleared == 0) {
            return LineClearResult(
                board = board,
                linesCleared = 0
            )
        }

        val newEmptyRows = List(linesCleared) { rowIndex ->
            createEmptyRow(
                row = rowIndex,
                columns = board.columns
            )
        }

        val shiftedRows = incompleteRows.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->
                cell.copy(
                    position = Position(
                        row= rowIndex + linesCleared,
                        column = columnIndex
                    )
                )
            }
        }

        val newCells = newEmptyRows + shiftedRows

        return LineClearResult(
            board = board.copy(cells = newCells),
            linesCleared = linesCleared
        )
    }

    fun calculateScore(linesCleared: Int): Int {
        return when (linesCleared) {
            1 -> 100
            2 -> 300
            3 -> 500
            4 -> 800
            else -> 0
        }
    }
}
