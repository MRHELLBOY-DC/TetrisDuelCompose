package com.example.tetrisduel.domain.logic

import com.example.tetrisduel.domain.models.Piece
import com.example.tetrisduel.domain.models.PieceType
import com.example.tetrisduel.domain.models.Position
import com.example.tetrisduel.domain.models.Rotation

class PieceGenerator {
    private var pieceBag : MutableList<PieceType> = mutableListOf()

    private fun refillBag(){
        pieceBag = PieceType.values()
            .toMutableList()
            .also { it.shuffle() }
    }

    fun generatePiece(
        spawnPosition: Position = Position(row= 0, column = 4)
    ): Piece{
        if(pieceBag.isEmpty()){
            refillBag()
        }

        val pieceType = pieceBag.removeAt(0)

        return Piece(
            type = pieceType,
            pivot = spawnPosition,
            rotation = Rotation.DEGREE_0
        )
    }

}


