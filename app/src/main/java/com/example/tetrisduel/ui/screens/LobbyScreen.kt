package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tetrisduel.ui.components.ConnectionStatusView
import com.example.tetrisduel.ui.viewmodels.LobbyNavigationEvent
import com.example.tetrisduel.ui.viewmodels.LobbyViewModel

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = hiltViewModel(),
    onNavigateToGame: (playerName: String, roomId: String, isOnline: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LobbyNavigationEvent.NavigateToGame -> {
                    onNavigateToGame(event.playerName, event.roomId, event.isOnline)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TETRIS DUEL",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ConnectionStatusView(isConnected = uiState.isConnectedToServer)

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.playerName,
                    onValueChange = viewModel::onPlayerNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre del jugador") },
                    singleLine = true,
                    enabled = !uiState.isLoading && !uiState.isWaitingForOpponent
                )

                OutlinedTextField(
                    value = uiState.roomCode,
                    onValueChange = viewModel::onRoomCodeChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Código de sala") },
                    singleLine = true,
                    enabled = !uiState.isLoading && !uiState.isWaitingForOpponent
                )

                uiState.generatedRoomCode?.let { code ->
                    Text(
                        text = "Sala creada: $code",
                        color = Color(0xFF22C55E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                uiState.errorMessage?.let { msg ->
                    Text(text = msg, color = Color(0xFFEF4444))
                }

                if (uiState.isWaitingForOpponent) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Esperando oponente...")
                    }
                } else if (uiState.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (uiState.isConnectedToServer) "Creando sala..." else "Conectando al servidor...",
                            color = Color(0xFF9CA3AF)
                        )
                    }
                } else {
                    Button(
                        onClick = viewModel::playLocal,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Jugar local")
                    }

                    Button(
                        onClick = viewModel::createRoom,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear sala")
                    }

                    TextButton(
                        onClick = viewModel::joinRoom,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unirse a sala")
                    }
                }
            }
        }
    }
}