package com.example.tetrisduel.domain.models

data class Position(
    val row: Int,
    val column: Int,
){
    fun moveBy(rowOffset: Int, columnOffset: Int): Position{
        return copy(
            row = row + rowOffset,
            column = column + columnOffset
        )
    }
}


/*
Para controlar la direccion en una cuadricula

*/
