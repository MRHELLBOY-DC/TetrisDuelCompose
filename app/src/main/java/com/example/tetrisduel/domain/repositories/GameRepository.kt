package com.example.tetrisduel.domain.repositories

import com.example.tetrisduel.data.socket.SocketEvent
import kotlinx.coroutines.flow.SharedFlow

interface GameRepository {
    val events: SharedFlow<SocketEvent>
    val isConnected: Boolean
    fun connect()
    fun disconnect()
    fun createRoom()
    fun joinRoom(roomId: String)
    fun sendAttack(roomId: String, garbageLines: Int)
    fun sendGameOver(roomId: String)
}