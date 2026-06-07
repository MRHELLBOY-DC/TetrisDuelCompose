package com.example.tetrisduel.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.tetrisduel.domain.logic.PieceRotationManager
import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.CellState
import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.PieceType
import com.example.tetrisduel.domain.models.Position

private fun isVisibleInsideBoard(
    board: Board,
    position: Position
): Boolean {
    return position.row in 0 until board.rows &&
            position.column in 0 until board.columns
}

private fun getCellColor(
    state: CellState,
    pieceType: PieceType?
): Color {
    return when (state) {
        CellState.EMPTY -> Color.Transparent
        CellState.GARBAGE -> Color(0xFF6B7280)
        CellState.FILLED -> getPieceColor(pieceType)
    }
}

private fun getPieceColor(pieceType: PieceType?): Color {
    return when (pieceType) {
        PieceType.I -> Color(0xFF22D3EE)
        PieceType.O -> Color(0xFFFACC15)
        PieceType.T -> Color(0xFFA855F7)
        PieceType.S -> Color(0xFF22C55E)
        PieceType.Z -> Color(0xFFEF4444)
        PieceType.J -> Color(0xFF3B82F6)
        PieceType.L -> Color(0xFFF97316)
        null -> Color.White
    }
}

@Composable
fun TetrisBoardCanvas(
    board: Board,
    currentPiece: Piece?,
    modifier: Modifier = Modifier
) {
    val pieceRotationManager = remember { PieceRotationManager() }

    Canvas(modifier = modifier) {
        val cellSize = minOf(
            size.width / board.columns,
            size.height / board.rows
        )

        val boardWidth = cellSize * board.columns
        val boardHeight = cellSize * board.rows

        val startX = (size.width - boardWidth) / 2f
        val startY = (size.height - boardHeight) / 2f

        fun drawBlock(row: Int, column: Int, color: Color) {
            val x = startX + column * cellSize
            val y = startY + row * cellSize

            drawRect(
                color = color,
                topLeft = Offset(x + 1f, y + 1f),
                size = Size(
                    width = cellSize - 2f,
                    height = cellSize - 2f
                )
            )
        }

        drawRect(
            color = Color(0xFF111827),
            topLeft = Offset(startX, startY),
            size = Size(boardWidth, boardHeight)
        )

        board.cells.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell.state != CellState.EMPTY) {
                    drawBlock(
                        row = rowIndex,
                        column = columnIndex,
                        color = getCellColor(
                            state = cell.state,
                            pieceType = cell.pieceType
                        )
                    )
                }
            }
        }

        currentPiece?.let { piece ->
            val positions = pieceRotationManager.getPiecePositions(piece)

            positions.forEach { position ->
                if (isVisibleInsideBoard(board, position)) {
                    drawBlock(
                        row = position.row,
                        column = position.column,
                        color = getPieceColor(piece.type)
                    )
                }
            }
        }

        for (row in 0..board.rows) {
            val y = startY + row * cellSize

            drawLine(
                color = Color(0xFF374151),
                start = Offset(startX, y),
                end = Offset(startX + boardWidth, y),
                strokeWidth = 1f
            )
        }

        for (column in 0..board.columns) {
            val x = startX + column * cellSize

            drawLine(
                color = Color(0xFF374151),
                start = Offset(x, startY),
                end = Offset(x, startY + boardHeight),
                strokeWidth = 1f
            )
        }
    }
}
