package com.example.tetrisduel.domain.logic

class ScoreCalculator {

    fun calculateLineClearScore(
        linesCleared: Int,
        level: Int = 1
    ): Int {
        val baseScore = when (linesCleared) {
            1 -> 100
            2 -> 300
            3 -> 500
            4 -> 800
            else -> 0
        }

        return baseScore * level
    }

    fun calculateSoftDropScore(cellsDropped: Int): Int {
        return cellsDropped
    }

    fun calculateHardDropScore(cellsDropped: Int): Int {
        return cellsDropped * 2
    }

    fun calculateLevel(totalLinesCleared: Int): Int {
        return (totalLinesCleared / 10) + 1
    }
}