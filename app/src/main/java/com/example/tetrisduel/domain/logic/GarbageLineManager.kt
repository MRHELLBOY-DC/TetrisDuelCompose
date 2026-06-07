package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.Cell
import com.example.tetrisduel.domain.models.CellState
import com.example.tetrisduel.domain.models.Position
import kotlin.random.Random

class GarbageLineManager {
    fun calculateGarbageLines(linesCleared: Int) : Int {
        return when(linesCleared) {
            1 -> 0
            2 -> 1
            3 -> 2
            4 -> 4
            else -> 0
        }
    }

    fun wouldCauseGameOver(board: Board, amount: Int): Boolean {
        val safeAmount = amount.coerceIn(0, board.rows)

        return board.cells
            .take(safeAmount)
            .any { row ->
                row.any { cell -> cell.state != CellState.EMPTY }
            }
    }

    fun createGarbageRow(
        row: Int,
        columns: Int
    ): List<Cell>{
        val holeColumn = Random.nextInt(columns)

        return List(columns) { column ->
            if (column == holeColumn) {
                Cell(
                    position = Position(row = row, column = column),
                    state = CellState.EMPTY,
                    pieceType = null
                )
            }else{
                Cell(
                    position = Position(row = row, column = column),
                    state = CellState.GARBAGE,
                    pieceType = null
                )
            }
        }
    }

    fun addGarbageLines(
        board: Board,
        amount: Int
    ): Board {
        if (amount <= 0 )return board

        val safeAmount = amount.coerceAtMost(board.rows)

        val remainingRows = board.cells.drop(safeAmount)

        val garbageRows = List(safeAmount) { index ->
            createGarbageRow(
                row = remainingRows.size + index,
                columns = board.columns
            )

        }

        val shiftedRows = remainingRows.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->
                cell.copy(
                    position = Position(
                        row = rowIndex,
                        column = columnIndex
                    )
                )
            }
        }

        return board.copy(
            cells = shiftedRows + garbageRows
        )
    }
}