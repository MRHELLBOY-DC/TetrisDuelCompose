package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Board
import com.example.tetrisduel.domain.models.Piece

class PieceMovementManager(
    private val collisionDetector: CollisionDetector
) {

    fun moveLeft(board: Board, piece: Piece): Piece {
        val movedPiece = piece.copy(
            pivot = piece.pivot.moveBy(
                rowOffset = 0,
                columnOffset = -1
            )
        )

        return if (collisionDetector.canPlacePiece(board, movedPiece)) {
            movedPiece
        } else {
            piece
        }
    }

    fun moveRight(board: Board, piece: Piece): Piece {
        val movedPiece = piece.copy(
            pivot = piece.pivot.moveBy(
                rowOffset = 0,
                columnOffset = 1
            )
        )

        return if (collisionDetector.canPlacePiece(board, movedPiece)) {
            movedPiece
        } else {
            piece
        }
    }

    fun moveDown(board: Board, piece: Piece): Piece {
        val movedPiece = piece.copy(
            pivot = piece.pivot.moveBy(
                rowOffset = 1,
                columnOffset = 0
            )
        )

        return if (collisionDetector.canPlacePiece(board, movedPiece)) {
            movedPiece
        } else {
            piece
        }
    }

    fun canMoveDown(board: Board, piece: Piece): Boolean {
        val movedPiece = piece.copy(
            pivot = piece.pivot.moveBy(
                rowOffset = 1,
                columnOffset = 0
            )
        )

        return collisionDetector.canPlacePiece(board, movedPiece)
    }

    fun hardDrop(board: Board, piece: Piece): Piece {
        var currentPiece = piece

        while (canMoveDown(board, currentPiece)) {
            currentPiece = currentPiece.copy(
                pivot = currentPiece.pivot.moveBy(
                    rowOffset = 1,
                    columnOffset = 0
                )
            )
        }

        return currentPiece
    }
}