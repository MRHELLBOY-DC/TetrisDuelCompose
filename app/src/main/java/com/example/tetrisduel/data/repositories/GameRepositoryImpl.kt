package com.example.tetrisduel.data.repositories

import com.example.tetrisduel.data.socket.SocketEvent
import com.example.tetrisduel.data.socket.SocketManager
import com.example.tetrisduel.domain.repositories.GameRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val socketManager: SocketManager
) : GameRepository {

    override val events: SharedFlow<SocketEvent> = socketManager.events
    override val isConnected: Boolean get() = socketManager.isConnected

    override fun connect() = socketManager.connect()
    override fun disconnect() = socketManager.disconnect()
    override fun createRoom() = socketManager.createRoom()
    override fun joinRoom(roomId: String) = socketManager.joinRoom(roomId)
    override fun sendAttack(roomId: String, garbageLines: Int) = socketManager.sendAttack(roomId, garbageLines)
    override fun sendGameOver(roomId: String) = socketManager.sendGameOver(roomId)
}