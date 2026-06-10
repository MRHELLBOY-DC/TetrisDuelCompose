package com.example.tetrisduel.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.tetrisduel.domain.logic.PieceRotationManager
import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.PieceType
import com.example.tetrisduel.domain.models.Position

private fun getNextPieceColor(type: PieceType): Color = when (type) {
    PieceType.I -> Color(0xFF22D3EE)
    PieceType.O -> Color(0xFFFACC15)
    PieceType.T -> Color(0xFFA855F7)
    PieceType.S -> Color(0xFF22C55E)
    PieceType.Z -> Color(0xFFEF4444)
    PieceType.J -> Color(0xFF3B82F6)
    PieceType.L -> Color(0xFFF97316)
}

@Composable
fun NextPieceCanvas(
    piece: Piece?,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
) {
    val rotationManager = remember { PieceRotationManager() }

    Canvas(modifier = modifier) {
        drawRect(color = Color(0xFF111827), topLeft = Offset.Zero, size = size)

        if (piece == null) return@Canvas

        val previewCols = 4
        val previewRows = 4
        val cellSize = minOf(size.width / previewCols, size.height / previewRows)
        val startX = (size.width - cellSize * previewCols) / 2f
        val startY = (size.height - cellSize * previewRows) / 2f

        val relPositions = rotationManager.getRelativePositions(piece.type, piece.rotation)
        val color = getNextPieceColor(piece.type)

        relPositions.forEach { rel ->
            val displayCol = rel.column + 1
            val displayRow = rel.row + 1
            if (displayRow in 0 until previewRows && displayCol in 0 until previewCols) {
                drawRect(
                    color = color,
                    topLeft = Offset(startX + displayCol * cellSize + 1f, startY + displayRow * cellSize + 1f),
                    size = Size(cellSize - 2f, cellSize - 2f)
                )
            }
        }
    }
}