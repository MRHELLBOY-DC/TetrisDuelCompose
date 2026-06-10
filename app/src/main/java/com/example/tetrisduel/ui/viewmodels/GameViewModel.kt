package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetrisduel.data.socket.SocketEvent
import com.example.tetrisduel.domain.logic.GarbageLineManager
import com.example.tetrisduel.domain.logic.TetrisEngine
import com.example.tetrisduel.domain.models.GameResult
import com.example.tetrisduel.domain.models.GameState
import com.example.tetrisduel.domain.repositories.GameRepository
import com.example.tetrisduel.ui.states.GameUiState
import com.example.tetrisduel.ui.states.ResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playerName: String = savedStateHandle["playerName"] ?: "Jugador"
    private val roomId: String = savedStateHandle["roomCode"] ?: ""
    private val isOnline: Boolean = savedStateHandle["isOnline"] ?: false

    private val tetrisEngine = TetrisEngine()
    private val garbageLineManager = GarbageLineManager()

    private val _gameState = MutableStateFlow(
        tetrisEngine.createInitialState(
            localPlayerId = "local",
            localPlayerName = playerName
        )
    )
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _uiState = MutableStateFlow(
        GameUiState(isOnline = isOnline, isOpponentConnected = isOnline)
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _navigateToResult = MutableSharedFlow<ResultUiState>(extraBufferCapacity = 1)
    val navigateToResult: SharedFlow<ResultUiState> = _navigateToResult.asSharedFlow()

    private val gameStartTime = System.currentTimeMillis()
    private var resultTriggered = false

    init {
        startGameLoop()
        if (isOnline) observeSocketEvents()
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                val current = _gameState.value
                if (!current.isGameRunning) break

                val level = (current.localPlayer.linesCleared / 10) + 1
                val speed = maxOf(100L, 800L - (level - 1) * 80L)
                delay(speed)

                val old = _gameState.value
                if (!old.isGameRunning) break

                _gameState.update { tetrisEngine.tick(it) }
                processStateChange(old, _gameState.value)
            }
        }
    }

    private fun observeSocketEvents() {
        viewModelScope.launch {
            gameRepository.events.collect { event ->
                when (event) {
                    is SocketEvent.Connected -> {
                        _uiState.update { it.copy(isConnectedToServer = true) }
                    }
                    is SocketEvent.Disconnected -> {
                        _uiState.update { it.copy(isConnectedToServer = false) }
                    }
                    is SocketEvent.ReceiveAttack -> {
                        val old = _gameState.value
                        _gameState.update {
                            tetrisEngine.addGarbageToLocalPlayer(it, event.garbageLines)
                        }
                        processStateChange(old, _gameState.value)
                    }
                    is SocketEvent.Victory -> {
                        _gameState.update { it.copy(isGameRunning = false, result = GameResult.WIN) }
                        triggerResultNavigation(GameResult.WIN)
                    }
                    is SocketEvent.OpponentDisconnected -> {
                        _uiState.update { it.copy(isOpponentConnected = false) }
                        _gameState.update { it.copy(isGameRunning = false, result = GameResult.WIN) }
                        triggerResultNavigation(GameResult.WIN)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun processStateChange(oldState: GameState, newState: GameState) {
        val deltaLines = newState.localPlayer.linesCleared - oldState.localPlayer.linesCleared
        if (deltaLines > 0) {
            if (isOnline && roomId.isNotBlank()) {
                val garbage = garbageLineManager.calculateGarbageLines(deltaLines)
                if (garbage > 0) gameRepository.sendAttack(roomId, garbage)
            }
            if (oldState.localPlayer.linesCleared < 37 && newState.localPlayer.linesCleared >= 37) {
                _uiState.update { it.copy(show37Banner = true) }
                viewModelScope.launch {
                    delay(3000L)
                    _uiState.update { it.copy(show37Banner = false) }
                }
            }
        }

        if (!newState.isGameRunning && newState.result == GameResult.LOSE) {
            if (isOnline && roomId.isNotBlank()) gameRepository.sendGameOver(roomId)
            triggerResultNavigation(GameResult.LOSE)
        }
    }

    private fun triggerResultNavigation(result: GameResult) {
        if (resultTriggered) return
        resultTriggered = true
        val state = _gameState.value
        val duration = (System.currentTimeMillis() - gameStartTime) / 1000L
        val winnerName = if (result == GameResult.WIN) playerName else "Oponente"
        _navigateToResult.tryEmit(
            ResultUiState(
                winnerName = winnerName,
                isLocalPlayerWinner = result == GameResult.WIN,
                score = state.localPlayer.score,
                linesCleared = state.localPlayer.linesCleared,
                durationSeconds = duration
            )
        )
    }

    fun moveLeft() = _gameState.update { tetrisEngine.moveLeft(it) }

    fun moveRight() = _gameState.update { tetrisEngine.moveRight(it) }

    fun moveDown() {
        val old = _gameState.value
        _gameState.update { tetrisEngine.moveDown(it) }
        processStateChange(old, _gameState.value)
    }

    fun hardDrop() {
        val old = _gameState.value
        _gameState.update { tetrisEngine.hardDrop(it) }
        processStateChange(old, _gameState.value)
    }

    fun rotateClockwise() = _gameState.update { tetrisEngine.rotateClockwise(it) }

    fun rotateCounterClockwise() = _gameState.update { tetrisEngine.rotateCounterClockwise(it) }
}