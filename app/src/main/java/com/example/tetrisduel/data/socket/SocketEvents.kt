package com.example.tetrisduel.data.socket

object  SocketEvents {
    // Cliente → Servidor
    const val CREATE_ROOM = "create_room"
    const val JOIN_ROOM = "join_room"
    const val SEND_ATTACK = "send_attack"
    const val GAME_OVER = "game_over"

    // Servidor → Cliente
    const val ROOM_CREATED = "room_created"
    const val GAME_START = "game_start"
    const val RECEIVE_ATTACK = "receive_attack"
    const val VICTORY = "victory"
    const val OPPONENT_DISCONNECTED = "opponent_disconnected"
    const val ERROR_MESSAGE = "error_message"
}

sealed class SocketEvent {
    object Connected : SocketEvent()
    object Disconnected : SocketEvent()
    data class RoomCreated(val roomId: String) : SocketEvent()
    object GameStart : SocketEvent()
    data class ReceiveAttack(val garbageLines: Int) : SocketEvent()
    object Victory : SocketEvent()
    object OpponentDisconnected : SocketEvent()
    data class ErrorMessage(val message: String) : SocketEvent()
}