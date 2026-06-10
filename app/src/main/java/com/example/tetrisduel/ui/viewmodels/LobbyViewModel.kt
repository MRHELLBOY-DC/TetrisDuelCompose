package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetrisduel.data.socket.SocketEvent
import com.example.tetrisduel.domain.repositories.GameRepository
import com.example.tetrisduel.ui.states.LobbyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LobbyNavigationEvent {
    data class NavigateToGame(
        val playerName: String,
        val roomId: String,
        val isOnline: Boolean
    ) : LobbyNavigationEvent()
}

private enum class PendingAction { NONE, CREATE_ROOM, JOIN_ROOM }

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LobbyNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<LobbyNavigationEvent> = _navigationEvent.asSharedFlow()

    private var pendingAction = PendingAction.NONE

    init {
        gameRepository.connect()
        observeSocketEvents()
    }

    private fun observeSocketEvents() {
        viewModelScope.launch {
            gameRepository.events.collect { event ->
                when (event) {
                    is SocketEvent.Connected -> {
                        _uiState.update { it.copy(isConnectedToServer = true) }
                        // Ejecuta la acción que estaba pendiente por falta de conexión
                        when (pendingAction) {
                            PendingAction.CREATE_ROOM -> { pendingAction = PendingAction.NONE; gameRepository.createRoom() }
                            PendingAction.JOIN_ROOM -> { pendingAction = PendingAction.NONE; gameRepository.joinRoom(_uiState.value.roomCode.trim()) }
                            PendingAction.NONE -> {}
                        }
                    }
                    is SocketEvent.Disconnected -> {
                        _uiState.update {
                            it.copy(
                                isConnectedToServer = false,
                                isWaitingForOpponent = false,
                                isLoading = false
                            )
                        }
                    }
                    is SocketEvent.RoomCreated -> {
                        _uiState.update {
                            it.copy(
                                generatedRoomCode = event.roomId,
                                roomCode = event.roomId,
                                isLoading = false,
                                isWaitingForOpponent = true
                            )
                        }
                    }
                    is SocketEvent.GameStart -> {
                        _uiState.update { it.copy(isWaitingForOpponent = false, isLoading = false) }
                        val state = _uiState.value
                        _navigationEvent.tryEmit(
                            LobbyNavigationEvent.NavigateToGame(
                                playerName = state.playerName.trim(),
                                roomId = state.roomCode.trim(),
                                isOnline = true
                            )
                        )
                    }
                    is SocketEvent.ErrorMessage -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = event.message,
                                isLoading = false,
                                isWaitingForOpponent = false
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun onPlayerNameChange(name: String) {
        _uiState.update { it.copy(playerName = name, errorMessage = null) }
    }

    fun onRoomCodeChange(roomCode: String) {
        _uiState.update { it.copy(roomCode = roomCode.uppercase(), errorMessage = null) }
    }

    fun playLocal() {
        val playerName = _uiState.value.playerName.trim()
        if (playerName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa tu nombre para jugar.") }
            return
        }
        _navigationEvent.tryEmit(
            LobbyNavigationEvent.NavigateToGame(
                playerName = playerName,
                roomId = "",
                isOnline = false
            )
        )
    }

    fun createRoom() {
        val playerName = _uiState.value.playerName.trim()
        if (playerName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa tu nombre antes de crear una sala.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        if (!gameRepository.isConnected) {
            pendingAction = PendingAction.CREATE_ROOM
            gameRepository.connect()
            return
        }
        gameRepository.createRoom()
    }

    fun joinRoom() {
        val playerName = _uiState.value.playerName.trim()
        val roomId = _uiState.value.roomCode.trim()
        if (playerName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa tu nombre.") }
            return
        }
        if (roomId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa el código de sala.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        if (!gameRepository.isConnected) {
            pendingAction = PendingAction.JOIN_ROOM
            gameRepository.connect()
            return
        }
        gameRepository.joinRoom(roomId)
    }
}