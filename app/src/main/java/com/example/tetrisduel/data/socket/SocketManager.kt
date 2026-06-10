package com.example.tetrisduel.data.socket

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var socket: Socket? = null

    private val _events = MutableSharedFlow<SocketEvent>(extraBufferCapacity = 32)
    val events: SharedFlow<SocketEvent> = _events

    val isConnected: Boolean get() = socket?.connected() == true

    fun connect() {
        if (socket?.connected() == true) return

        val opts = IO.Options.builder()
            .setTransports(arrayOf("websocket"))
            .build()

        socket = IO.socket(SocketConfig.SERVER_URL, opts).apply {
            on(Socket.EVENT_CONNECT) {
                _events.tryEmit(SocketEvent.Connected)
            }
            on(Socket.EVENT_DISCONNECT) {
                _events.tryEmit(SocketEvent.Disconnected)
            }
            on(SocketEvents.ROOM_CREATED) { args ->
                val roomId = (args.firstOrNull() as? JSONObject)?.optString("roomId") ?: ""
                if (roomId.isNotBlank()) _events.tryEmit(SocketEvent.RoomCreated(roomId))
            }
            on(SocketEvents.GAME_START) {
                _events.tryEmit(SocketEvent.GameStart)
            }
            on(SocketEvents.RECEIVE_ATTACK) { args ->
                val garbageLines = (args.firstOrNull() as? JSONObject)?.optInt("garbageLines") ?: 0
                if (garbageLines > 0) _events.tryEmit(SocketEvent.ReceiveAttack(garbageLines))
            }
            on(SocketEvents.VICTORY) {
                _events.tryEmit(SocketEvent.Victory)
            }
            on(SocketEvents.OPPONENT_DISCONNECTED) {
                _events.tryEmit(SocketEvent.OpponentDisconnected)
            }
            on(SocketEvents.ERROR_MESSAGE) { args ->
                val message = (args.firstOrNull() as? JSONObject)?.optString("message") ?: "Error"
                _events.tryEmit(SocketEvent.ErrorMessage(message))
            }
            connect()
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun createRoom() {
        socket?.emit(SocketEvents.CREATE_ROOM)
    }

    fun joinRoom(roomId: String) {
        socket?.emit(SocketEvents.JOIN_ROOM, JSONObject().apply {
            put("roomId", roomId)
        })
    }

    fun sendAttack(roomId: String, garbageLines: Int) {
        socket?.emit(SocketEvents.SEND_ATTACK, JSONObject().apply {
            put("roomId", roomId)
            put("garbageLines", garbageLines)
        })
    }

    fun sendGameOver(roomId: String) {
        socket?.emit(SocketEvents.GAME_OVER, JSONObject().apply {
            put("roomId", roomId)
        })
    }
}