package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.PieceType
import com.example.tetrisduel.domain.models.Position
import com.example.tetrisduel.domain.models.Rotation

class PieceRotationManager {
    fun rotateClockwise(piece: Piece): Piece{
        val newRotation = when(piece.rotation){
            Rotation.DEGREE_0 -> Rotation.DEGREE_90
            Rotation.DEGREE_90 -> Rotation.DEGREE_180
            Rotation.DEGREE_180 -> Rotation.DEGREE_270
            Rotation.DEGREE_270 -> Rotation.DEGREE_0
        }

        return piece.copy(rotation = newRotation)
    }

    fun rotateCounterClockwise(piece: Piece): Piece{
        val newRotation = when(piece.rotation){
            Rotation.DEGREE_0 -> Rotation.DEGREE_270
            Rotation.DEGREE_90 -> Rotation.DEGREE_0
            Rotation.DEGREE_180 -> Rotation.DEGREE_90
            Rotation.DEGREE_270 -> Rotation.DEGREE_180
        }

        return piece.copy(rotation = newRotation)
    }


    fun getPiecePositions(piece: Piece): List<Position> {
        val pivot = piece.pivot

        return getRelativePositions(piece.type, piece.rotation).map { relativePosition ->
            Position(
                row = pivot.row + relativePosition.row,
                column = pivot.column + relativePosition.column
            )
        }
    }

    fun getRelativePositions(
        type: PieceType,
        rotation: Rotation
    ): List <Position>{
        return when (type) {
            PieceType.I -> getIPositions(rotation)
            PieceType.O -> getOPositions()
            PieceType.T -> getTPositions(rotation)
            PieceType.S -> getSPositions(rotation)
            PieceType.Z -> getZPositions(rotation)
            PieceType.J -> getJPositions(rotation)
            PieceType.L -> getLPositions(rotation)
        }
    }

    private fun getIPositions(rotation: Rotation): List<Position>{
        /*
        Rotación 180:
                  0
                  o
                  o
                  o

        Rotación 270:
            o o o o

         */

        return when(rotation){
            Rotation.DEGREE_0,
            Rotation.DEGREE_180 -> listOf(
                Position(0,-1),
                Position(0, 0),
                Position(0, 1),
                Position(0, 2)
            )
            Rotation.DEGREE_90,
            Rotation.DEGREE_270 -> listOf(
                    Position(-1, 0),
                    Position(0, 0),
                    Position(1, 0),
                    Position(2, 0)
                )
        }
    }

    private fun getOPositions(): List<Position> {
        /*
            0 0
            0 0
         */

        return listOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    private fun getTPositions(rotation: Rotation): List<Position>{
        /*
        Rotación 0:
            o
            o o
            o

        Rotación 90:
            o o o
              o

        Rotación 180:
              o
            o o
              o

        Rotación 270:
              o
            o o o

         */
        return when(rotation){
            Rotation.DEGREE_0 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(0, 1),
                Position(1,0 )
            )
            Rotation.DEGREE_90 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(1, 0),
                Position(0,-1)
            )
            Rotation.DEGREE_180 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(0, 1),
                Position(-1,0 )
            )
            Rotation.DEGREE_270 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(1, 0),
                Position(0, 1)
            )
        }
    }

    private fun getSPositions(rotation: Rotation): List<Position> {
        /*
        Rotación 180:
                o
                o  o
                   o

        Rotación 270:
              o o
            o o
         */

        return when (rotation) {
            Rotation.DEGREE_0,
            Rotation.DEGREE_180 -> listOf(
                Position(0, 0),
                Position(0, 1),
                Position(1, -1),
                Position(1, 0)
            )

            Rotation.DEGREE_90,
            Rotation.DEGREE_270 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(0, 1),
                Position(1, 1)
            )
        }
    }

    private fun getZPositions(rotation: Rotation): List<Position> {
        return when (rotation) {
            Rotation.DEGREE_0,
            Rotation.DEGREE_180 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(1, 0),
                Position(1, 1)
            )

            Rotation.DEGREE_90,
            Rotation.DEGREE_270 -> listOf(
                Position(-1, 1),
                Position(0, 0),
                Position(0, 1),
                Position(1, 0)
            )
        }
    }

    private fun getJPositions(rotation: Rotation): List<Position> {
        /*
            Rotación 0°:

                o
                o
                o o

            Rotación 90°:

                o o o
                o

            Rotación 180°:

                o o
                  o
                  o

            Rotación 270°:

                    o
                o o o
         */

        return when (rotation) {
            Rotation.DEGREE_0 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(0, 1),
                Position(1, -1)
            )

            Rotation.DEGREE_90 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(1, 0),
                Position(-1, -1)
            )

            Rotation.DEGREE_180 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(0, 1),
                Position(-1, 1)
            )

            Rotation.DEGREE_270 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(1, 0),
                Position(1, 1)
            )
        }
    }

    private fun getLPositions(rotation: Rotation): List<Position> {
        /*
        Rotación 0°:

                  o
                  o
                o o

        Rotación 90°:

                o
                o o o

        Rotación 180°:

                o o
                o
                o

        Rotación 270°:

                o o o
                    o
         */
        return when (rotation) {
            Rotation.DEGREE_0 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(0, 1),
                Position(1, 1)
            )

            Rotation.DEGREE_90 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(1, 0),
                Position(1, -1)
            )

            Rotation.DEGREE_180 -> listOf(
                Position(0, -1),
                Position(0, 0),
                Position(0, 1),
                Position(-1, -1)
            )

            Rotation.DEGREE_270 -> listOf(
                Position(-1, 0),
                Position(0, 0),
                Position(1, 0),
                Position(-1, 1)
            )
        }
    }

}