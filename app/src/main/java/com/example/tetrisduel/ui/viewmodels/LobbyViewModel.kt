package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tetrisduel.ui.states.LobbyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class LobbyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    fun onPlayerNameChange(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                playerName = name,
                errorMessage = null
            )
        }
    }

    fun onRoomCodeChange(roomCode: String) {
        _uiState.update { currentState ->
            currentState.copy(
                roomCode = roomCode.uppercase(),
                errorMessage = null
            )
        }
    }

    fun createRoom() {
        val playerName = _uiState.value.playerName.trim()

        if (playerName.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Ingresa tu nombre antes de crear una sala.")
            }
            return
        }

        val roomCode = generateRoomCode()

        _uiState.update { currentState ->
            currentState.copy(
                generatedRoomCode = roomCode,
                roomCode = roomCode,
                errorMessage = null
            )
        }
    }

    fun canPlayLocal(): Boolean {
        return _uiState.value.playerName.trim().isNotBlank()
    }

    fun canJoinRoom(): Boolean {
        return _uiState.value.playerName.trim().isNotBlank() &&
                _uiState.value.roomCode.trim().isNotBlank()
    }

    private fun generateRoomCode(): String {
        val number = Random.nextInt(1000, 9999)
        return "ROOM$number"
    }
}