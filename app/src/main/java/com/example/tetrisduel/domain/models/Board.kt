package com.example.tetrisduel.domain.models

data class Board(
    val rows: Int = 20,
    val columns: Int = 10,
    val cells: List<List<Cell>> = createEmptyCells()
){
    companion object {
        fun createEmptyCells(
            rows: Int = 20,
            columns: Int = 10
        ): List<List<Cell>>{
            return List(rows){ row ->
                List(columns) { column ->
                    Cell(
                        position = Position(
                            row = row,
                            column = column
                        )
                    )
                }
            }
        }
    }
}


