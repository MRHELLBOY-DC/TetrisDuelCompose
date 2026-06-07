package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetrisduel.ui.components.TetrisBoardCanvas
import com.example.tetrisduel.ui.viewmodels.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Score: ${gameState.localPlayer.score}"
        )

        TetrisBoardCanvas(
            board = gameState.localPlayer.board,
            currentPiece = gameState.currentPiece,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(10f / 20f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.moveLeft() }) {
                Text("←")
            }

            Button(onClick = { viewModel.rotateClockwise() }) {
                Text("⟳")
            }

            Button(onClick = { viewModel.moveRight() }) {
                Text("→")
            }

            Button(onClick = { viewModel.moveDown() }) {
                Text("↓")
            }

            Button(onClick = { viewModel.hardDrop() }) {
                Text("DROP")
            }
        }
    }
}