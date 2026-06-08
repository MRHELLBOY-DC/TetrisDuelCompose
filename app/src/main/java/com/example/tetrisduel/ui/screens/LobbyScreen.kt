package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetrisduel.ui.viewmodels.LobbyViewModel

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = viewModel(),
    onStartGame: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tetris Duel")

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.playerName,
                    onValueChange = viewModel::onPlayerNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre del jugador") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.roomCode,
                    onValueChange = viewModel::onRoomCodeChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Código de sala") },
                    singleLine = true
                )

                uiState.generatedRoomCode?.let { roomCode ->
                    Text(text = "Sala creada: $roomCode")
                }

                uiState.errorMessage?.let { message ->
                    Text(text = message)
                }

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                }

                Button(
                    onClick = {
                        if (viewModel.canPlayLocal()) {
                            onStartGame()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Jugar local")
                }

                Button(
                    onClick = {
                        viewModel.createRoom()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear sala")
                }

                TextButton(
                    onClick = {
                        if (viewModel.canJoinRoom()) {
                            onStartGame()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Unirse a sala")
                }
            }
        }
    }
}