package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetrisduel.domain.logic.TetrisEngine
import com.example.tetrisduel.domain.models.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val tetrisEngine = TetrisEngine()

    private val _gameState = MutableStateFlow(
        tetrisEngine.createInitialState(
            localPlayerId = "local-player",
            localPlayerName = "Player 1"
        )
    )

    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startGameLoop()
    }

    fun moveLeft() {
        _gameState.update { currentState ->
            tetrisEngine.moveLeft(currentState)
        }
    }

    fun moveRight() {
        _gameState.update { currentState ->
            tetrisEngine.moveRight(currentState)
        }
    }

    fun moveDown() {
        _gameState.update { currentState ->
            tetrisEngine.moveDown(currentState)
        }
    }

    fun hardDrop() {
        _gameState.update { currentState ->
            tetrisEngine.hardDrop(currentState)
        }
    }

    fun rotateClockwise() {
        _gameState.update { currentState ->
            tetrisEngine.rotateClockwise(currentState)
        }
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                delay(800L)

                _gameState.update { currentState ->
                    tetrisEngine.tick(currentState)
                }
            }
        }
    }
}